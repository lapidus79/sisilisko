package common.services.akka;

import actors.ActorRoutes;
import actors.dashboard.DashboardActorProtocol;
import actors.dashboard.DashboardManagerActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.inject.Inject;
import models.Dashboard;

public class DashboardAkkaFacade extends AkkaFacade {

    @Inject
    public DashboardAkkaFacade(final ActorSystem actorSystem) {
        super(actorSystem);
    }

    public Dashboard addDashboard(final Dashboard dashboard) {
        ActorRef ref = lookup(ActorRoutes.DASHBOARD_MANAGER_PATH);
        DashboardManagerActorProtocol.StartDashboard msg =
                new DashboardManagerActorProtocol.StartDashboard(dashboard.getTokenId());
        ref.tell(msg, ActorRef.noSender());
        return dashboard;
    }

    public Dashboard terminateDashboard(final Dashboard dashboard) {
        ActorRef ref = lookup(ActorRoutes.DASHBOARD_MANAGER_PATH);
        DashboardManagerActorProtocol.TerminateDashboard msg =
                new DashboardManagerActorProtocol.TerminateDashboard(dashboard.getTokenId());
        ref.tell(msg, ActorRef.noSender());
        return dashboard;
    }

    public Dashboard refreshDashboard(final Dashboard dashboard) {
        ActorRef ref = lookup(buildPath(ActorRoutes.dashboardActorPath(dashboard.getTokenId())));
        DashboardActorProtocol.LoadDashboardCmd msg = new DashboardActorProtocol.LoadDashboardCmd();
        ref.tell(msg, ActorRef.noSender());
        return dashboard;
    }

}
