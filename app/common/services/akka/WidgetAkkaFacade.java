package common.services.akka;

import actors.ActorRoutes;
import actors.dashboard.DashboardActorProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.inject.Inject;
import models.Widget;

public class WidgetAkkaFacade extends AkkaFacade {

    @Inject
    public WidgetAkkaFacade(final ActorSystem actorSystem) {
        super(actorSystem);
    }

    public Widget addWidget(final Widget w) {
        ActorRef ref = lookup(buildPath(ActorRoutes.dashboardActorPath(w.getDashboard().getTokenId())));
        DashboardActorProtocol.LaunchWidgetCmd msg = new DashboardActorProtocol.LaunchWidgetCmd(w.getId());
        ref.tell(msg, ActorRef.noSender());
        return w;
    }

    public Widget terminateWidget(final Widget w) {
        ActorRef ref = lookup(buildPath(ActorRoutes.dashboardActorPath(w.getDashboard().getTokenId())));
        DashboardActorProtocol.TerminateWidgetCmd msg = new DashboardActorProtocol.TerminateWidgetCmd(w.getId());
        ref.tell(msg, ActorRef.noSender());
        return w;
    }

    public Widget refreshWidget(final Widget w) {
        ActorRef ref = lookup(buildPath(ActorRoutes.widgetActorPath(w.getDashboard().getTokenId(), w.getId())));
        WidgetProtocol.Load msg = new WidgetProtocol.Load();
        ref.tell(msg, ActorRef.noSender());
        return w;
    }

}
