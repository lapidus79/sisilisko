package views;

import play.data.validation.Constraints;

public class DashboardForm {

    @Constraints.Required(message="dashboard.name.required")
    protected String name;

    public DashboardForm() {
    }

    public DashboardForm(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
