package common.seed;

import models.Dashboard;

public class SeedDashboard {

    public static Dashboard buildDashboard(String tokenId, String name) {
        Dashboard d = new Dashboard();
        d.setTokenId(tokenId);
        d.setName(name);
        return d;
    }

}
