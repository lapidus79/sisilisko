package actors.widget.ping;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.*;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Results.ok;

public class PingWorkerActorTest {

    private static ActorSystem system;
    private WSClient ws;
    private Server server;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @After
    public void eachDown() throws IOException {
        try { ws.close(); }
        finally { server.stop(); }
    }

    @Test
    public void pingWhenHttpResponse() {
        this.server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello").routeTo(() -> ok("yeah"))
                .build());
        this.ws = play.test.WSTestClient.newClient(server.httpPort());
        String endpoint = "http://localhost:" + server.httpPort() + "/hello";

        new TestKit(system) {{
            ActorRef pingWorker = buildWorker(ws, this);

            PingWorkerProtocol.PingCmd executeCmd =  new PingWorkerProtocol.PingCmd(endpoint, 1000L);
            long start = System.currentTimeMillis();
            pingWorker.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("1 second"));
            assertThat(msg).isInstanceOf(PingWorkerProtocol.PingEvt.class);
            PingWorkerProtocol.PingEvt evt = (PingWorkerProtocol.PingEvt) msg;
            assertThat((evt).statusCode).isEqualTo(Http.Status.OK);
            assertThat((evt).latency).isGreaterThan(0L);
            assertThat((evt).timestamp).isGreaterThanOrEqualTo(start);
        }};
    }

    @Test
    public void pingWhenTimeout() {
        this.server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello")
                .routeAsync(() -> { try {
                    Thread.sleep(5L);
                    return CompletableFuture.completedFuture(ok("yeah"));
                } catch (Exception e) { throw new RuntimeException(e); }
        })
                .build());
        this.ws = play.test.WSTestClient.newClient(server.httpPort());
        String endpoint = "http://localhost:" + server.httpPort() + "/hello";

        new TestKit(system) {{
            ActorRef pingWorker = buildWorker(ws, this);

            PingWorkerProtocol.PingCmd executeCmd =  new PingWorkerProtocol.PingCmd(endpoint, 1L);
            pingWorker.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("1 second"));
            assertThat(msg).isInstanceOf(PingWorkerProtocol.PingEvt.class);
            PingWorkerProtocol.PingEvt evt = (PingWorkerProtocol.PingEvt) msg;
            assertThat((evt).statusCode).isEqualTo(PingWorkerActor.STATUS_REQUEST_TIMEOUT);
        }};
    }

    @Test
    public void pingToNonOpenPort() {
        this.server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello").routeTo(() -> ok("yeah"))
                .build());
        this.ws = play.test.WSTestClient.newClient(server.httpPort());
        String closedPort = "http://localhost:40000/hello";

        new TestKit(system) {{
            ActorRef pingWorker = buildWorker(ws, this);

            PingWorkerProtocol.PingCmd executeCmd =  new PingWorkerProtocol.PingCmd(closedPort, 1000L);
            pingWorker.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("2 second"));
            assertThat(msg).isInstanceOf(PingWorkerProtocol.PingEvt.class);
            PingWorkerProtocol.PingEvt evt = (PingWorkerProtocol.PingEvt) msg;
            assertThat((evt).statusCode).isEqualTo(PingWorkerActor.STATUS_CONNECTION_FAILED);
        }};
    }

    @Test
    public void pingToUnknownHost() {
        this.server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello").routeTo(() -> ok("yeah"))
                .build());
        this.ws = play.test.WSTestClient.newClient(server.httpPort());
        String unknownHost = "http://gotallyunknownhostadjakjhj.com";

        new TestKit(system) {{
            ActorRef pingWorker = buildWorker(ws, this);

            PingWorkerProtocol.PingCmd executeCmd =  new PingWorkerProtocol.PingCmd(unknownHost, 1000L);
            pingWorker.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("1 second"));
            assertThat(msg).isInstanceOf(PingWorkerProtocol.PingEvt.class);
            PingWorkerProtocol.PingEvt evt = (PingWorkerProtocol.PingEvt) msg;
            assertThat((evt).statusCode).isEqualTo(PingWorkerActor.STATUS_HOST_UNKNOWN);
        }};
    }

    @Test
    public void pingToMalformedURL() {
        this.server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello").routeTo(() -> ok("yeah"))
                .build());
        this.ws = play.test.WSTestClient.newClient(server.httpPort());
        String malformed = "this is a malformed uri without protocol";

        new TestKit(system) {{
            ActorRef pingWorker = buildWorker(ws, this);
            PingWorkerProtocol.PingCmd executeCmd =  new PingWorkerProtocol.PingCmd(malformed, 1000L);
            pingWorker.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("1 second"));
            assertThat(msg).isInstanceOf(PingWorkerProtocol.PingEvt.class);
            PingWorkerProtocol.PingEvt evt = (PingWorkerProtocol.PingEvt) msg;
            assertThat((evt).statusCode).isEqualTo(PingWorkerActor.STATUS_CONF_INVALID);
            assertThat((evt).latency).isEqualTo(-1L);
        }};
    }

    private ActorRef buildWorker(WSClient ws, TestKit testKit) {
        final Props pingWorkerProps = Props.create(PingWorkerActor.class, ws);
        return testKit.childActorOf(pingWorkerProps, "worker");
    }
}

