package actors.widget.ping;


import actors.dao.WidgetDAOProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.Actor;

public class PingWidgetProtocol {

    /**
     * The current state of our ping widget
     */
    public static class StateEvt extends WidgetProtocol.StateEvt {
        public final String url;
        public final Integer interval;
        public final Integer statusCode;
        public final Long timestamp;
        public final Long latency;
        // When ping result updated
        public StateEvt(final StateEvt curState,
                        final PingWorkerProtocol.PingEvt ping) {
            super(curState.id, curState.dashboardId, curState.dashboardTokenId, curState.name, curState.type, curState.status);
            this.url = curState.url;
            this.interval = curState.interval;
            this.statusCode = ping.statusCode;
            this.timestamp = ping.timestamp;
            this.latency = ping.latency;
        }
        // When status is updated
        public StateEvt(final StateEvt curState,
                        final String status) {
            super(curState.id, curState.dashboardId, curState.dashboardTokenId, curState.name, curState.type, status);
            this.url = curState.url;
            this.interval = curState.interval;
            this.statusCode = (curState != null) ? curState.statusCode : null;
            this.timestamp = (curState != null) ? curState.timestamp : null;
            this.latency = (curState != null) ? curState.latency : null;
        }
        // When DAO is updated
        public StateEvt(final StateEvt curState,
                        final WidgetDAOProtocol.PingWidgetDAO widgetDAO,
                        final String status) {
            super(widgetDAO.id, widgetDAO.dashboardId, widgetDAO.dashboardTokenId, widgetDAO.name, widgetDAO.type, status);
            this.url = widgetDAO.url;
            this.interval = widgetDAO.interval;
            this.statusCode = (curState != null) ? curState.statusCode : null;
            this.timestamp = (curState != null) ? curState.timestamp : null;
            this.latency = (curState != null) ? curState.latency : null;
        }

    }

    public interface Factory {
        Actor create();
    }

}
