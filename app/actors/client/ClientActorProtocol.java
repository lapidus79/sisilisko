package actors.client;

import actors.dashboard.DashboardActorProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.Actor;
import akka.actor.ActorRef;

public class ClientActorProtocol {

    public static final String DASHBOARD_UPDATE = "dashboard_update";
    public static final String WIDGET_UPDATE = "widget_update";

    public static class ClientMsgEvt {
        public final String command;
        public final Object payload;
        public ClientMsgEvt(final DashboardActorProtocol.StateEvt payload) {
            this.payload = payload;
            this.command = DASHBOARD_UPDATE;
        }
        public ClientMsgEvt(final WidgetProtocol.StateEvt payload) {
            this.payload = payload;
            this.command = WIDGET_UPDATE;
        }
    }

    public interface Factory {
        Actor create(ActorRef out);
    }

}
