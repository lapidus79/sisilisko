package views;


import models.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetListJson extends ArrayList<WidgetJson> {

    public WidgetListJson(final List<Widget> widgetList) {
        this.addAll(
                widgetList
                        .stream()
                        .map(WidgetJson::new)
                        .collect(Collectors.toList())
        );
    }
}
