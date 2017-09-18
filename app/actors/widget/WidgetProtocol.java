package actors.widget;


import akka.actor.ActorRef;

public class WidgetProtocol {

    /**
     * Reload widget meta from db
     */
    public static class Load {
        public Load() {}
    }

    /**
     * Execute the widget 'loop' once (e.g. perform ping for ping widget etc)
     */
    public static class Execute {
        public Execute() {}
    }

    /**
     * Request the widget state for recipient
     */
    public static class RequestState {
        public final ActorRef recipient;
        public RequestState(final ActorRef recipient) {
            this.recipient = recipient;
        }
    }

    /**
     * Current state of the widget, see widget specific implementation
     */
    public abstract static class StateEvt {
        public final Long id;
        public final Long dashboardId;
        public final String dashboardTokenId;
        public final String name;
        public final String type;
        public final String status;
        public StateEvt(Long widgetId,
                        Long dashboardId,
                        String dashboardTokenId,
                        String name,
                        String type,
                        String status) {
            this.id = widgetId;
            this.dashboardId = dashboardId;
            this.dashboardTokenId = dashboardTokenId;
            this.name = name;
            this.type = type;
            this.status = status;
        }
    }

}
