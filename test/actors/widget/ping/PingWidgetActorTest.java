package actors.widget.ping;


import actors.widget.WidgetProtocol;
import actors.widget.ping.PingWidgetActor;
import actors.widget.ping.PingWidgetProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import models.Dashboard;
import models.Widget;
import models.repositories.WidgetRepository;
import models.widget.PingWidgetConf;
import org.junit.*;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.Optional;

import static common.seed.SeedDashboard.buildDashboard;
import static common.seed.SeedWidget.buildPingWidget;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Results.ok;

public class PingWidgetActorTest {
/*
    static ActorSystem system;
    private WSClient ws;
    private Server server;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create();
    }

    @Test
    public void executeToHostThatReturns200() {
        server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
                .GET("/hello").routeTo(() -> ok("yeah"))
                .build());
        ws = play.test.WSTestClient.newClient(server.httpPort());
        Dashboard dashboardOne = buildDashboard("TjuEC3WcGVgMALvLtjzaCfgJQeXBaPfg", "dashboardOne");
        dashboardOne.setId(1L);
        Widget widgetOne = buildPingWidget(dashboardOne,"widgetOne", "http://localhost:" + server.httpPort() + "/hello");
        widgetOne.setId(2L);
        ((PingWidgetConf)widgetOne.getWidgetConf()).setInterval(1);
        WidgetRepository repo = mock(WidgetRepository.class);
        when(repo.findById(widgetOne.getId())).thenReturn(Optional.of(widgetOne));

        new TestKit(system) {{
            final Props pingWidgetProps = Props.create(PingWidgetActor.class, ws, repo);
            ActorRef pingWidget = this.childActorOf(pingWidgetProps, widgetOne.getId().toString());

            PingWidgetProtocol.ExecuteCmd executeCmd =  new PingWidgetProtocol.ExecuteCmd();
            pingWidget.tell(executeCmd, getRef());

            Object msg = this.receiveOne(duration("3 second"));
            assertThat(msg).isInstanceOf(WidgetProtocol.UpdateEvt.class);
            WidgetProtocol.UpdateEvt wm = (WidgetProtocol.UpdateEvt) msg;
            assertThat((wm).name).isEqualTo(widgetOne.getName());
            assertThat((wm).type).isEqualTo(widgetOne.getType().toString());
            assertThat((wm).id).isEqualTo(widgetOne.getId());
            assertThat(((PingWidgetProtocol.PingEvt) wm.state).statusCode).isEqualTo(Http.Status.OK);
        }};
    }
*/
    /*
    @Test
    @Ignore
    public void executeWhenMalformedUrl() {

    }

    @Test
    @Ignore
    public void executeWhenUnknownHost() {

    }

    @Test
    @Ignore
    public void executeWhenStatusCodeReturned() {

    }

    @Test
    @Ignore
    public void executeWhenTimeout() {

    }

    @Test
    @Ignore
    public void reload() {

    }

    @Test
    @Ignore
    public void reloadConfRetrievalFails() {

    }
    */
/*
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
*/
}

