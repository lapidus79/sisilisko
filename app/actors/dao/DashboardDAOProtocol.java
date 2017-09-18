package actors.dao;


import akka.actor.Actor;
import models.Dashboard;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardDAOProtocol {

    /**
     * Fetch Dashboard from storage
     */
    public static class Fetch {
        public final String tokenId;
        public Fetch(final String tokenId) {
            this.tokenId = tokenId;
        }
    }

    /**
     * Fetch dashboardTokenId for all dashboards
     */
    public static class FetchAllDashboardTokenIds {
        public FetchAllDashboardTokenIds() {
        }
    }

    /**
     * Dashboard DAO model
     */
    public static class DashboardDAO {
        public final Long id;
        public final String tokenId;
        public final String name;
        public List<Long>  widgets;
        public DashboardDAO(final Dashboard dashboard) {
            this.id = dashboard.getId();
            this.tokenId = dashboard.getTokenId();
            this.name = dashboard.getName();
            this.widgets = dashboard.getWidgets().stream().map((w) -> w.getId()).collect(Collectors.toList());
        }
    }

    /**
     * DashboardTokenId list
     */
    public static class DashboardTokenIdList {
        public final List<String> dashboardTokenIds;
        public DashboardTokenIdList(final List<String> dashboardTokenIds) {
            this.dashboardTokenIds = dashboardTokenIds;
        }
    }

    /**
     * If any DAO operation fails we send this to ourselves
     * so that we can throw an exception from the actor and
     * initiate the supverision strategy
     */
    public static class Failed {
        public final String msg;
        public Failed(final String msg) {
            this.msg = msg;
        }
    }


    public interface Factory {
        public Actor create();
    }

}
