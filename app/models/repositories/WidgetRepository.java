package models.repositories;

import com.google.inject.ImplementedBy;
import models.Widget;

import java.util.List;
import java.util.Optional;

@ImplementedBy(WidgetRepositoryEbean.class)
public interface WidgetRepository {

    Optional<Widget> findById(final Long id);
    List<Widget> findAll(Long dashboardId);
    Widget save(Widget widget);
    Widget update(Widget widget);
    Widget delete(Widget widget);
}
