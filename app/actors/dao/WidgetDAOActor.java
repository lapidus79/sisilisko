package actors.dao;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import common.exceptions.application.ModelNotFoundException;
import models.repositories.WidgetRepository;
import play.libs.akka.InjectedActorSupport;

import java.util.concurrent.CompletableFuture;

public class WidgetDAOActor extends AbstractActor implements InjectedActorSupport {

    public static Props getProps() {
        return Props.create(WidgetDAOActor.class);
    }
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final WidgetRepository wRepo;

    @Inject
    public WidgetDAOActor(final WidgetRepository wRepo) {
        this.wRepo = wRepo;
        log.info("[{}].constructor(): starting...", getSelf().path());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WidgetDAOProtocol.Fetch.class, this::fetch)
                .match(WidgetDAOProtocol.Failed.class, this::fail)
                .build();
    }

    private void fetch(WidgetDAOProtocol.Fetch cmd) {
        log.info("[{}].fetch(): id=[{}]...", getSelf().path().name(), cmd.id);
        ActorRef recipient = getSender();
        CompletableFuture
                .supplyAsync(() -> wRepo.findById(cmd.id)
                        .orElseThrow(ModelNotFoundException::new), getContext().dispatcher())
                .thenApply(WidgetDAOProtocol::build)
                .thenAccept((dao) -> recipient.tell(dao, getSelf()))
                .exceptionally(throwable -> {
                    self().tell(new DashboardDAOProtocol.Failed(throwable.getMessage()), self());
                    return null;
                });
    }

    private void fail(WidgetDAOProtocol.Failed cmd) {
        throw new RuntimeException("WidgetDAOActor: failed with " + cmd.msg);
    }

}
