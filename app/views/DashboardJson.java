package views;

import models.Dashboard;

public class DashboardJson {

    public final Long id;
    public final String tokenId;
    public final String name;
    public final Long created;
    public final Long updated;

    public DashboardJson(final Dashboard dashboard) {
        this.id = dashboard.getId();
        this.tokenId = dashboard.getTokenId();
        this.name = dashboard.getName();
        this.created = dashboard.getCreated().getTime();
        this.updated = dashboard.getUpdated().getTime();
    }

}
