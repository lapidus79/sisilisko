package views;

import models.Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardListJson extends ArrayList<DashboardJson> {

    public DashboardListJson(final List<Dashboard> dashboardList) {
        this.addAll(
                dashboardList
                        .stream()
                        .map(DashboardJson::new)
                        .collect(Collectors.toList())
        );
    }
}
