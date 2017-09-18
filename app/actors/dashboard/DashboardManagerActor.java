package actors.dashboard;


import actors.ActorRoutes;
import actors.dao.DashboardDAOProtocol;
import actors.dao.WidgetDAOProtocol;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import com.google.inject.Inject;
import play.Logger;
import play.libs.akka.InjectedActorSupport;

import java.util.HashMap;

public class DashboardManagerActor extends AbstractActor implements InjectedActorSupport {

    private final DashboardActorProtocol.Factory dashboardFactory;
    private HashMap<String, ActorRef> idToDashboardRefMap = new HashMap<>();
    private HashMap<ActorRef, String> dashboardRefToIdMap = new HashMap<>();

    public static Props getProps() {
        return Props.create(DashboardManagerActor.class);
    }

    @Inject
    public DashboardManagerActor(final DashboardActorProtocol.Factory dashboardFactory) {
        this.dashboardFactory = dashboardFactory;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DashboardManagerActorProtocol.StartDashboard.class, this::startDashboard)
                .match(DashboardManagerActorProtocol.TerminateDashboard.class, this::terminateDashboard)
                .match(DashboardManagerActorProtocol.ReloadDashboards.class, this::fetchDashboards)
                .match(DashboardDAOProtocol.DashboardTokenIdList.class, this::addDashboards)
                .match(Terminated.class, this::childTerminatedEvent)
                .build();
    }

    private void startDashboard(DashboardManagerActorProtocol.StartDashboard msg) {
        Logger.info("[{}].startDashboard(): id=[{}]", getSelf().path().name(), msg.dashboardTokenId);
        initBoard(msg.dashboardTokenId);
    }

    private void terminateDashboard(DashboardManagerActorProtocol.TerminateDashboard msg) {
        Logger.info("[{}].terminateDashboard(): id=[{}]", getSelf().path().name(), msg.dashboardTokenId);
        if (idToDashboardRefMap.containsKey(msg.dashboardTokenId)) {
            ActorRef ref = idToDashboardRefMap.get(msg.dashboardTokenId);
            getContext().stop(ref);
            idToDashboardRefMap.remove(msg.dashboardTokenId);
            dashboardRefToIdMap.remove(ref);
        }
    }

    private void fetchDashboards(DashboardManagerActorProtocol.ReloadDashboards msg) {
        Logger.info("[{}].fetchDashboards(): requesting list of dashboards ", getSelf().path().name());
        ActorRef daoRef = getContext().getSystem().actorFor(ActorRoutes.DASHBOARD_DAO_PATH);
        daoRef.tell(new DashboardDAOProtocol.FetchAllDashboardTokenIds(), self());
    }

    private void addDashboards(DashboardDAOProtocol.DashboardTokenIdList evt) {
        long added = evt.dashboardTokenIds.stream()
                .map(this::initBoard)
                .filter((b) -> b)
                .count();
        Logger.info("[{}].addDashboards(): total=[{}] added=[{}]",
                getSelf().path().name(),
                evt.dashboardTokenIds.size(),
                added);
    }

    private void childTerminatedEvent(Terminated msg) {
        Logger.info("[{}].childTerminatedEvent(): child=[{}]", getSelf().path().name(), msg.getActor().path().name());
        String tokenId = dashboardRefToIdMap.get(msg.getActor());
        if (tokenId != null && idToDashboardRefMap.containsKey(tokenId)) {
            idToDashboardRefMap.remove(tokenId);
        }
        if (dashboardRefToIdMap.containsKey(msg.getActor())) {
            dashboardRefToIdMap.remove(msg.getActor());
        }
    }

    private boolean initBoard(String dTokenId) {
        if (!idToDashboardRefMap.containsKey(dTokenId)) {
            ActorRef dRef = injectedChild(() -> dashboardFactory.create(), dTokenId);
            getContext().watch(dRef);
            idToDashboardRefMap.put(dTokenId, dRef);
            dashboardRefToIdMap.put(dRef, dTokenId);
            return true;
        }
        return false;
    }
}


