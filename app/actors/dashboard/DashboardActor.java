package actors.dashboard;


import actors.ActorRoutes;
import actors.dao.DashboardDAOProtocol;
import actors.widget.ping.PingWidgetProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import play.libs.akka.InjectedActorSupport;

import java.util.HashMap;
import java.util.HashSet;

public class DashboardActor extends AbstractActor implements InjectedActorSupport {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DELETED = "DELETED";
    public static Props getProps() {
        return Props.create(DashboardActor.class);
    }

    private final PingWidgetProtocol.Factory widgetFactory;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private HashMap<Long, ActorRef> idToWidgetRefMap = new HashMap<>();
    private HashMap<ActorRef, Long> widgetRefToIdMap = new HashMap<>();
    private HashSet<ActorRef> subscribers = new HashSet<>();
    private DashboardActorProtocol.StateEvt state = null;

    @Inject
    public DashboardActor(final PingWidgetProtocol.Factory widgetFactory) {
        this.widgetFactory = widgetFactory;
    }

    @Override
    public void preStart() {
        log.info("[{}].preStart(): path=[{}]", getDashboardTokenId(), getSelf().path());
        self().tell(new DashboardActorProtocol.LoadDashboardCmd(), self());
    }

    @Override
    public void postStop() {
        log.info("[{}].postStop(): stopped", getDashboardTokenId());
        this.state = new DashboardActorProtocol.StateEvt(state, STATUS_DELETED);
        notifySubscribers(state);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DashboardActorProtocol.SubscribeActor.class, this::subscribe)
                .match(DashboardActorProtocol.UnsubscribeActor.class, this::unsubscribe)
                .match(DashboardActorProtocol.LoadDashboardCmd.class, this::loadDAO)
                .match(DashboardActorProtocol.TerminateWidgetCmd.class, this::terminateWidget)
                .match(DashboardActorProtocol.LaunchWidgetCmd.class, this::launchWidget)
                .match(DashboardDAOProtocol.DashboardDAO.class, this::updateDashboard)
                .match(WidgetProtocol.StateEvt.class, this::forwardToSubscribers) // forward widget updates to subscribers
                .match(Terminated.class, this::childTerminatedEvent)
                .build();
    }

    private void subscribe(DashboardActorProtocol.SubscribeActor cmd) {
        log.info("[{}].subscribe(): client=[{}]", getSelf().path().name(), sender().path().name());
        subscribers.add(sender());
        sender().tell(state, self());
        widgetRefToIdMap.keySet()
                .forEach(w -> w.tell(new WidgetProtocol.RequestState(sender()), sender()));
    }

    private void unsubscribe(DashboardActorProtocol.UnsubscribeActor cmd) {
        log.info("[{}].unsubscribe(): client=[{}]", getSelf().path().name(), sender().path().name());
        subscribers.remove(getSender());
    }

    private void launchWidget(DashboardActorProtocol.LaunchWidgetCmd msg) {
        log.info("[{}].launchWidget(): starting widget=[{}]", getSelf().path().name(), msg.widgetId);
        initWidgetIfNotInitialized(msg.widgetId);
    }

    private void terminateWidget(DashboardActorProtocol.TerminateWidgetCmd msg) {
        log.info("[{}].terminateWidget(): terminating widget=[{}]", getSelf().path().name(), msg.widgetId);
        if (idToWidgetRefMap.containsKey(msg.widgetId)) {
            ActorRef ref = idToWidgetRefMap.get(msg.widgetId);
            getContext().stop(ref);
            idToWidgetRefMap.remove(msg.widgetId);
            widgetRefToIdMap.remove(ref);
        }
    }

    private void loadDAO(DashboardActorProtocol.LoadDashboardCmd cmd) {
        log.info("[{}].loadDAO(): requesting dao ", getDashboardTokenId());
        ActorRef daoRef = getContext().getSystem().actorFor(ActorRoutes.DASHBOARD_DAO_PATH);
        daoRef.tell(new DashboardDAOProtocol.Fetch(getDashboardTokenId()), self());
    }

    private void updateDashboard(DashboardDAOProtocol.DashboardDAO evt) {
        this.state = new DashboardActorProtocol.StateEvt(evt, STATUS_ACTIVE);
        notifySubscribers(state);
        evt.widgets.forEach(this::initWidgetIfNotInitialized);
    }

    private void childTerminatedEvent(Terminated msg) {
        log.info("[{}].childTerminatedEvent(): child=[{}]", getDashboardTokenId(), msg.getActor().path().name());
        Long tokenId = widgetRefToIdMap.get(msg.getActor());
        if (tokenId != null && idToWidgetRefMap.containsKey(tokenId)) {
            idToWidgetRefMap.remove(tokenId);
        }
        if (widgetRefToIdMap.containsKey(msg.getActor())) {
            widgetRefToIdMap.remove(msg.getActor());
        }
    }

    private void forwardToSubscribers(WidgetProtocol.StateEvt evt) {
        notifySubscribers(evt);
    }

    private void initWidgetIfNotInitialized(Long wId) {
        if (!idToWidgetRefMap.containsKey(wId)) {
            ActorRef wRef = injectedChild(widgetFactory::create, wId.toString());
            getContext().watch(wRef);
            idToWidgetRefMap.put(wId, wRef);
            widgetRefToIdMap.put(wRef, wId);
        } else {
            log.error("[{}].initWidget(): not starting widget, because " +
                    "widget actor already exists [{}]", getDashboardTokenId(), wId);
        }
    }

    private void notifySubscribers(Object evt) {
        subscribers.forEach(watcher -> watcher.tell(evt, self()));
    }

    private String getDashboardTokenId() {
        return self().path().name();
    }

}
