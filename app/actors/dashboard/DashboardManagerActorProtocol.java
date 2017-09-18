package actors.dashboard;


public class DashboardManagerActorProtocol {

    /**
     * A dashboard should be removed from the akka cluster
     */
    public static class TerminateDashboard {
        public final String dashboardTokenId;
        public TerminateDashboard(final String dashboardTokenId) {
            this.dashboardTokenId = dashboardTokenId;
        }
    }

    /**
     * A dashboard should be started in the akka cluster
     */
    public static class StartDashboard {
        public final String dashboardTokenId;
        public StartDashboard(final String dashboardTokenId) {
            this.dashboardTokenId = dashboardTokenId;
        }
    }

    /**
     * Kill all current dashboards and the reinitialize from storage
     */
    public static class ReloadDashboards {
        public ReloadDashboards() {
        }
    }

}
