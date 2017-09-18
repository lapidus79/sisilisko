package actors.dao;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import common.exceptions.application.ModelNotFoundException;
import models.Dashboard;
import models.repositories.DashboardRepository;
import play.libs.akka.InjectedActorSupport;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardDAOActor extends AbstractActor implements InjectedActorSupport {

    public static Props getProps() {
        return Props.create(DashboardDAOActor.class);
    }
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final DashboardRepository boardRepo;

    @Inject
    public DashboardDAOActor(final DashboardRepository boardRepo) {
        this.boardRepo = boardRepo;
        log.info("[{}].constructor(): started...", getSelf().path().name());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DashboardDAOProtocol.Fetch.class, this::fetch)
                .match(DashboardDAOProtocol.FetchAllDashboardTokenIds.class, this::fetchAllDashboardTokenIds)
                .build();
    }

    private void fetch(DashboardDAOProtocol.Fetch cmd) {
        if (log.isInfoEnabled()) {
            log.info("[{}].fetch(): fetching dashboardTokenId=[{}]", getSelf().path().name(), cmd.tokenId);
        }
        Dashboard d = boardRepo.findByTokenId(cmd.tokenId).orElseThrow(ModelNotFoundException::new);
        getSender().tell(new DashboardDAOProtocol.DashboardDAO(d), getSelf());
    }

    private void fetchAllDashboardTokenIds(DashboardDAOProtocol.FetchAllDashboardTokenIds cmd) {
        if (log.isInfoEnabled()) {
            log.info("[{}].fetchAllDashboardTokenIds(): fetching all..", getSelf().path().name());
        }
        List<Dashboard> boards = boardRepo.findAll(); // TODO -- Do this in batches...
        List<String> ids = boards.stream().map(Dashboard::getTokenId).collect(Collectors.toList());
        getSender().tell(new DashboardDAOProtocol.DashboardTokenIdList(ids), self());
    }



}
