package common.seed;

import io.ebean.Ebean;
import io.ebean.Model;
import models.Dashboard;
import models.Widget;
import play.Logger;

public class Seed {

    public static void dropAndSeedDb() {
        Logger.info("Dropping existing database");
        emptyDatabase();
        Logger.info("Seeding database");
        buildSeedData();
        Logger.info("Seeding completed");
    }

    private static void emptyDatabase() {
        Ebean.createSqlUpdate("DELETE from widget").execute();
        Ebean.createSqlUpdate("DELETE from dashboard").execute();
    }

    private static void buildSeedData() {

        for (int i = 0; i < 1; i++) {
            Dashboard d = store(SeedDashboard.buildDashboard("abcdefghijklmnopqrstuvwxyz00000" + i, "dashboard-"+i));
            for (int j = 0; j < 2; j++) {
                Widget w = store(SeedWidget.buildPingWidget(d, "widget-"+i+"-"+j, "https://www.google.fi"));
            }
        }
    }

    public static <T extends Model> T store(T m) {
        m.save();
        return m;
    }

}
