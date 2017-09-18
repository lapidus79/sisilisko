package controllers;

import actors.ActorRoutes;
import actors.client.ClientManagerProtocol;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.Pair;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.reactivestreams.Publisher;
import play.Logger;
import play.libs.F;
import play.mvc.*;
import scala.compat.java8.FutureConverters;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletionStage;

public class WsCtrl extends Controller {

    private Materializer materializer;
    private ActorRef clientManagerActor;

    @Inject
    public WsCtrl(Materializer materializer,
                  @Named(ActorRoutes.CLIENT_MANAGER_NAME) ActorRef clientManagerActor) {
        this.materializer = materializer;
        this.clientManagerActor = clientManagerActor;
    }

    public WebSocket ws() {
        return WebSocket.Json.acceptOrResult(request -> {
            Logger.info("WebSocket: new connection detected id=[{}]", request().asScala().id());
            final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
            final CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> stage = future.thenApplyAsync(F.Either::Right);
            return stage.exceptionally(this::logException);
        });
    }

    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
        final Pair<ActorRef, Publisher<JsonNode>> pair = createWebSocketConnections();
        ActorRef webSocketOutRef = pair.first();
        Publisher<JsonNode> webSocketIn = pair.second();

        String id = String.valueOf(request.asScala().id());
        final CompletionStage<ActorRef> wsOutActorFuture = createUserActor(id, webSocketOutRef);

        final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> flow = wsOutActorFuture
                .thenApplyAsync(wsOutActor -> createWebSocketFlow(webSocketIn, wsOutActor));
        return flow;
    }

    private Pair<ActorRef, Publisher<JsonNode>> createWebSocketConnections() {
        // Step 1: Create a source which is materialized as an ActorRef -> so we can send it messages from our akkasystem
        final Source<JsonNode, ActorRef> source = Source.actorRef(10, OverflowStrategy.dropTail());

        // Step 2: Create sink materialized as a publisher.
        final Sink<JsonNode, Publisher<JsonNode>> sink = Sink.asPublisher(AsPublisher.WITHOUT_FANOUT);

        // Step 3: Combine the source and sink into a flow, telling it to keep the materialized values, and then kicks the flow into existence.
        final Pair<ActorRef, Publisher<JsonNode>> pair = source.toMat(sink, Keep.both()).run(materializer);
        return pair;
    }

    private CompletionStage<ActorRef> createUserActor(String id, ActorRef webSocketOutRef) {
        long timeoutMillis = 100L;
        ClientManagerProtocol.CreateClient createMsg = new ClientManagerProtocol.CreateClient(id, webSocketOutRef);
        return FutureConverters
                .toJava(ask(clientManagerActor, createMsg, timeoutMillis))
                .thenApply(respObj -> (ActorRef) respObj);
    }

    private Flow<JsonNode, JsonNode, NotUsed> createWebSocketFlow(Publisher<JsonNode> webSocketIn, ActorRef userActor) {
        // Sink: outgoing traffic i.e userActor -> websocketOut -> play -> browser ws events
        final Sink<JsonNode, NotUsed> sink = Sink.actorRef(userActor, new Status.Success("success"));
        // Source: browser -> ws -> play -> publisher ->  userActor
        final Source<JsonNode, NotUsed> source = Source.fromPublisher(webSocketIn);

        final Flow<JsonNode, JsonNode, NotUsed> flow = Flow.fromSinkAndSource(sink, source);

        // Handle connection termination event
        return flow.watchTermination((ignore, termination) -> {
            termination.whenComplete((done, throwable) -> {
                Logger.info("WebSocket: connection terminated by client=[{}]", userActor.path().name());
                clientManagerActor.tell(new ClientManagerProtocol.TerminateClient(userActor), userActor);
            });
            return NotUsed.getInstance();
        });
    }

    private F.Either<Result, Flow<JsonNode, JsonNode, ?>> logException(Throwable throwable) {
        Logger.error("Cannot create websocket", throwable);
        Result result = Results.internalServerError("error");
        return F.Either.Left(result);
    }

}
