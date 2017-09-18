package models.repositories;

import com.google.inject.ImplementedBy;
import models.Dashboard;

import java.util.List;
import java.util.Optional;

@ImplementedBy(DashboardRepositoryEbean.class)
public interface DashboardRepository {

    Optional<Dashboard> findById(final Long id);
    Optional<Dashboard> findByTokenId(final String tokenId);
    List<Dashboard> findAll();
    Dashboard save(Dashboard dashboard);
    Dashboard update(Dashboard dashboard);
    Dashboard delete(Dashboard dashboard);
}
