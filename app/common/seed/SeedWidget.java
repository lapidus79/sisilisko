package common.seed;

import models.Dashboard;
import models.Widget;
import models.widget.PingWidgetConf;

public class SeedWidget {

    public static Widget buildPingWidget(Dashboard dashboard, String name, String url) {
        Widget w = new Widget();
        w.setName(name);
        w.setDashboard(dashboard);
        w.setType(Widget.Type.PING);
        PingWidgetConf conf = new PingWidgetConf(url);
        w.setWidgetConf(conf);
        return w;
    }

}
