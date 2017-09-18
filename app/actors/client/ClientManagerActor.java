package actors.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import play.Logger;
import play.libs.akka.InjectedActorSupport;

public class ClientManagerActor extends AbstractActor implements InjectedActorSupport {

    private final ClientActorProtocol.Factory clientFactory;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Inject
    public ClientManagerActor(final ClientActorProtocol.Factory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClientManagerProtocol.CreateClient.class, this::createClient)
                .match(ClientManagerProtocol.TerminateClient.class, this::terminateClient)
                .build();
    }

    private void createClient(final ClientManagerProtocol.CreateClient msg) {
        log.info("[{}].createClient(): remote connection is [{}]", self().path().name(), msg.out);
        ActorRef client = injectedChild(() -> clientFactory.create(msg.out), "client-" + msg.id);
        getSender().tell(client, self());
    }

    private void terminateClient(final ClientManagerProtocol.TerminateClient cmd) {
        log.info("[{}].terminateClient(): terminating=[{}]", self().path().name(), cmd.client);
        getContext().stop(cmd.client);
    }

}