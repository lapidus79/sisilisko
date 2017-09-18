package views;


import models.Widget;
import models.widget.WidgetConf;

public class WidgetJson {

    public final Long id;
    public final Widget.Type type;
    public final String name;
    public final Long created;
    public final Long updated;
    public final WidgetConf conf;

    public WidgetJson(final Widget widget) {
        this.id = widget.getId();
        this.type = widget.getType();
        this.name = widget.getName();
        this.created = widget.getCreated().getTime();
        this.updated = widget.getUpdated().getTime();
        this.conf = widget.getWidgetConf();
    }

}
