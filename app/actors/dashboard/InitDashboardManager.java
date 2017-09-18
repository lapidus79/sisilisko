package actors.dashboard;

import actors.ActorRoutes;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.TimeUnit;

public class InitDashboardManager {

    /**
     * Add small delay on app startup before initializing the actors
     */
    @Inject
    public InitDashboardManager(final ActorSystem system,
                                @Named(ActorRoutes.DASHBOARD_MANAGER_NAME) ActorRef actorRef) {
        int delaySec = 2;
        system.scheduler().scheduleOnce(
                Duration.create(delaySec, TimeUnit.SECONDS),
                actorRef,
                new DashboardManagerActorProtocol.ReloadDashboards(),
                system.dispatcher(),
                null
        );
    }
}
