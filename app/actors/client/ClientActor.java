package actors.client;

import actors.ActorRoutes;
import actors.dashboard.DashboardActorProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import play.libs.Json;

import java.util.HashMap;
import java.util.Optional;

/**
 * Maintains the client ws-connection and sends new data from the Dashboard actors to the client
 */
public class ClientActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final HashMap<String, ActorRef> subscriptions = new HashMap<>();
    private final ActorSystem system;
    private final ActorRef client;

    @Inject
    public ClientActor(final ActorSystem system,
                       final @Assisted ActorRef client) {
        this.system = system;
        this.client = client;
    }

    @Override
    public void postStop() {
        subscriptions
                .values()
                .forEach((d) -> d.tell(new DashboardActorProtocol.UnsubscribeActor(), getSelf()));
        log.info("[{}].postStop(): stop complete", self().path().name());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DashboardActorProtocol.StateEvt.class, this::sendDashboardUpdateToClient)
                .match(WidgetProtocol.StateEvt.class, this::sendWidgetUpdateToClient)
                .match(JsonNode.class, this::processClientMessage)
                .build();
    }

    private void sendDashboardUpdateToClient(final DashboardActorProtocol.StateEvt evt) {
        ClientActorProtocol.ClientMsgEvt msg = new ClientActorProtocol.ClientMsgEvt(evt);
        log.info("[{}].sendToClient(): json=[{}]", getSelf().path().name(), Json.stringify(Json.toJson(msg)));
        client.tell(Json.toJson(msg), self());
    }

    private void sendWidgetUpdateToClient(final WidgetProtocol.StateEvt evt) {
        ClientActorProtocol.ClientMsgEvt msg = new ClientActorProtocol.ClientMsgEvt(evt);
        log.info("[{}].sendToClient(): json=[{}]", getSelf().path().name(), Json.stringify(Json.toJson(msg)));
        client.tell(Json.toJson(msg), self());
    }

    private void processClientMessage(final JsonNode node) {
        Optional<String> cmd = parseVal(node, "command");
        Optional<String> val = parseVal(node, "value");

        if (cmd.isPresent()) {
            if (cmd.get().equalsIgnoreCase("SUBSCRIBE") && val.isPresent()) {
                subscribeToDashboard(val.get());
                return;
            } else if (cmd.get().equalsIgnoreCase("UNSUBSCRIBE") && val.isPresent()) {
                unsubscribeFromDashboard(val.get());
                return;
            } else if (cmd.get().equalsIgnoreCase("KEEPALIVE")) {
                // ignore
                return;
            }
        }
        log.info("[{}].processClientMessage(): Unknow/malformed msg received from client{}",
                    self().path().name(), Json.stringify(node));
    }

    private void subscribeToDashboard(String tokenId) {
        log.info("[{}].subscribeToDashboard(): dashboard=[{}]", getSelf().path().name(), tokenId);
        Optional<String> path = ActorRoutes.dashboardActorPath(tokenId);
        if (path.isPresent()) {
            ActorRef dashboardActor = system.actorFor(path.get());
            subscriptions.put(tokenId, dashboardActor);
            dashboardActor.tell(new DashboardActorProtocol.SubscribeActor(), self());
        }
    }

    private void unsubscribeFromDashboard(String tokenId) {
        log.info("[{}].unsubscribeFromDashboard(): dashboard=[{}]", getSelf().path().name(), tokenId);
        ActorRef dashboardActor = subscriptions.get(tokenId);
        subscriptions.remove(tokenId);
        if (dashboardActor != null && !dashboardActor.isTerminated()) {
            dashboardActor.tell(new DashboardActorProtocol.UnsubscribeActor(), self());
        }
    }

    private Optional<String> parseVal(JsonNode node, String field) {
        return node.has(field) ? Optional.ofNullable(node.get(field).asText()) : Optional.empty();
    }

}

