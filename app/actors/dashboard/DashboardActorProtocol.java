package actors.dashboard;


import actors.dao.DashboardDAOProtocol;
import akka.actor.Actor;

public class DashboardActorProtocol {

    /**
     * The current state of the dashboard
     */
    public static class StateEvt {
        public final Long id;
        public final String tokenId;
        public final String name;
        public final String status;
        public StateEvt(final DashboardDAOProtocol.DashboardDAO dao,
                        final String status) {
            this.id = dao.id;
            this.tokenId = dao.tokenId;
            this.name = dao.name;
            this.status = status;
        }
        public StateEvt(final StateEvt curState,
                        final String status) {
            this.id = curState.id;
            this.tokenId = curState.tokenId;
            this.name = curState.name;
            this.status = status;
        }
    }

    /**
     * Subscribe an actor to receive dashboard updates
     */
    public static class SubscribeActor {
        public SubscribeActor() { }
    }

    /**
     * Unsubscribe an actor from the dashboard
     */
    public static class UnsubscribeActor {
        public UnsubscribeActor() { }
    }

    /**
     * Reload the dashboard meta from the database (but don't touch widgets)
     */
    public static class LoadDashboardCmd {
        public LoadDashboardCmd() { }
    }

    /**
     * Removed widget from the dashboard
     */
    public static class TerminateWidgetCmd {
        public final Long widgetId;
        public TerminateWidgetCmd(final Long widgetId) {
            this.widgetId = widgetId;
        }
    }

    /**
     * Add a widget to dashboard
     */
    public static class LaunchWidgetCmd {
        public final Long widgetId;
        public LaunchWidgetCmd(final Long widgetId) {
            this.widgetId = widgetId;
        }
    }

    public interface Factory {
        public Actor create();
    }

}
