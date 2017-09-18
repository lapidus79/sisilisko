package actors.widget.ping;


import actors.ActorRoutes;
import actors.dao.WidgetDAOProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import play.libs.akka.InjectedActorSupport;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class PingWidgetActor extends AbstractActorWithTimers implements InjectedActorSupport {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DELETED = "DELETED";
    public static Props getProps() {
        return Props.create(PingWidgetActor.class);
    }

    private static final Object TIMER_KEY = "PING_KEY";
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final PingWorkerProtocol.Factory pingWorkerFactory;
    private ActorRef pingWorker;
    private PingWidgetProtocol.StateEvt state = null;

    @Inject
    public PingWidgetActor(final PingWorkerProtocol.Factory pingWorkerFactory) {
        this.pingWorkerFactory = pingWorkerFactory;
    }

    @Override
    public void preStart() {
        log.info("({}).preStart():", self().path().name());
        pingWorker = injectedChild(() -> pingWorkerFactory.create(), getWidgetId()+"-pingworker");
        getSelf().tell(new WidgetProtocol.Load(), getSelf());
    }

    @Override
    public void postStop() {
        log.info("[{}].postStop(): stopped", getSelf().path().name());
        this.state =  new PingWidgetProtocol.StateEvt(state, STATUS_DELETED);
        sendStateTo(getContext().getParent());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WidgetProtocol.Execute.class, this::executeWidget)
                .match(WidgetProtocol.Load.class, this::loadWidget)
                .match(WidgetProtocol.RequestState.class, this::sendState)
                .match(PingWorkerProtocol.PingEvt.class, this::updatePingStatus)
                .match(WidgetDAOProtocol.PingWidgetDAO.class, this::updateWidgetDAO)
                .build();
    }

    private void executeWidget(final WidgetProtocol.Execute cmd) {
        log.info("[{}].executeWidget():", self().path().name());
        PingWorkerProtocol.PingCmd c = new PingWorkerProtocol.PingCmd(state.url, 2000L);
        pingWorker.tell(c, self());
    }

    private void loadWidget(final WidgetProtocol.Load cmd) {
        log.info("[{}].loadWidget():", self().path().name());
        ActorRef daoRef = getContext().getSystem().actorFor(ActorRoutes.WIDGET_DAO_PATH);
        daoRef.tell(new WidgetDAOProtocol.Fetch(getWidgetId()), self());
    }

    private void sendState(final WidgetProtocol.RequestState cmd) {
        log.info("[{}].sendState(): recipient=[{}]", self().path().name(), cmd.recipient);
        sendStateTo(cmd.recipient);
    }

    private void updatePingStatus(final PingWorkerProtocol.PingEvt ping) {
        log.info("[{}].updatePingStatus(): status={} interval={}ms", self().path().name(), ping.statusCode, ping.latency);
        if (state == null || state.timestamp == null || state.timestamp < ping.timestamp) { // Only store / send the newest ping evt
            state = new PingWidgetProtocol.StateEvt(state,  ping);
            sendStateTo(getContext().getParent());
        }
    }

    private void updateWidgetDAO(final WidgetDAOProtocol.PingWidgetDAO dao) {
        log.info("[{}].updateWidgetDAO():", self().path().name());
        getTimers().cancel(TIMER_KEY);
        this.state = new PingWidgetProtocol.StateEvt(state, dao, STATUS_ACTIVE);
        FiniteDuration pollInterval = Duration.create(state.interval, TimeUnit.SECONDS);
        getTimers().startPeriodicTimer(TIMER_KEY, new WidgetProtocol.Execute(), pollInterval);
    }

    private void sendStateTo(ActorRef recipient) {
        recipient.tell(state, self());
    }

    private Long getWidgetId() {
        return Long.parseLong(getSelf().path().name());
    }

}

