package models.repositories;

import io.ebean.Ebean;
import models.Widget;

import java.util.List;
import java.util.Optional;


public class WidgetRepositoryEbean implements WidgetRepository {

    @Override
    public Optional<Widget> findById(final Long id) {
        return Optional.ofNullable(Ebean.find(Widget.class, id.longValue()));
    }

    @Override
    public List<Widget> findAll(final Long dashboardId) {
        return Widget.find.query().where().eq("dashboard_id", dashboardId).findList();
    }

    @Override
    public Widget save(final Widget widget) {
        widget.save();
        return widget;
    }

    @Override
    public Widget update(final Widget widget) {
        widget.update();
        return widget;
    }

    @Override
    public Widget delete(final Widget widget) {
        widget.delete();
        return widget;
    }

}
