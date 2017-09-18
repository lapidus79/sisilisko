package views;

import play.data.validation.Constraints;

public class WidgetForm {

    @Constraints.Required(message="widget.dashboardId.required")
    public Long dashboardId;
    @Constraints.Required(message="widget.name.required")
    public String name;
    @Constraints.Pattern(value="PING", message="widget.type.unknown")
    public String type;
    @Constraints.Required(message="widget.conf.required")
    public Object conf;

    public WidgetForm() {
    }

    public WidgetForm(final Long dashboardId,
                      final String name,
                      final String type,
                      Object conf) {
        this.dashboardId = dashboardId;
        this.name = name;
        this.type = type;
        this.conf = conf;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(final Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Object getConf() {
        return conf;
    }

    public void setConf(final Object conf) {
        this.conf = conf;
    }
}
