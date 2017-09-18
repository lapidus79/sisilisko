package models.repositories;

import io.ebean.Ebean;
import models.Dashboard;

import java.util.List;
import java.util.Optional;


public class DashboardRepositoryEbean implements DashboardRepository {

    @Override
    public Optional<Dashboard> findById(final Long id) {
        return Optional.ofNullable(Ebean.find(Dashboard.class, id.longValue()));
    }

    @Override
    public Optional<Dashboard> findByTokenId(final String tokenId) {
        return Dashboard.find.query().where().eq("token_id", tokenId).findOneOrEmpty();
    }

    @Override
    public List<Dashboard> findAll() {
        return Dashboard.find.all();
    }

    @Override
    public Dashboard save(final Dashboard dashboard) {
        dashboard.save();
        return dashboard;
    }

    @Override
    public Dashboard update(final Dashboard dashboard) {
        dashboard.update();
        return dashboard;
    }

    @Override
    public Dashboard delete(final Dashboard dashboard) {
        dashboard.delete();
        return dashboard;
    }

}
