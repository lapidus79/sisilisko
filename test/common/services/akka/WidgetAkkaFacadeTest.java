package common.services.akka;

import actors.dashboard.DashboardActorProtocol;
import actors.widget.WidgetProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;
import models.Dashboard;
import models.Widget;
import org.junit.Before;
import org.junit.Test;

import static akka.testkit.JavaTestKit.duration;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WidgetAkkaFacadeTest {

    String dashboardTokenId = "TjuEC3WcGVgMALvLtjzaCfgJQeXBaPfg";
    String invalidToken = "../../dadasadasas";
    Dashboard dashboard = new Dashboard();
    Widget widget = new Widget();
    WidgetAkkaFacade widgetAkkaFacade;
    ActorSystem system;
    ActorSystem systemReal;

    @Before
    public void setUp() {
        system = mock(ActorSystem.class);
        systemReal = ActorSystem.create();
        widgetAkkaFacade = new WidgetAkkaFacade(system);
        widget.setDashboard(dashboard);
        dashboard.setTokenId(dashboardTokenId);
    }

    @Test
    public void addWidget() {
        widget.setId(1L);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager/" + dashboardTokenId)).thenReturn(mock);
        widgetAkkaFacade.addWidget(widget);
        probe.expectMsgClass(duration("1 second"), DashboardActorProtocol.LaunchWidgetCmd.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWidget_whenDashboardTokenIdIsInvalid() {
        dashboard.setTokenId(invalidToken);
        widgetAkkaFacade.addWidget(widget);
    }

    @Test
    public void terminateWidget() {
        widget.setId(1L);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager/" + dashboardTokenId)).thenReturn(mock);
        widgetAkkaFacade.terminateWidget(widget);
        probe.expectMsgClass(duration("1 second"), DashboardActorProtocol.TerminateWidgetCmd.class);
    }

    @Test
    public void refreshDashboard() {
        widget.setId(1L);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager/" + dashboardTokenId + "/1")).thenReturn(mock);
        widgetAkkaFacade.refreshWidget(widget);
        probe.expectMsgClass(duration("1 second"), WidgetProtocol.Load.class);
    }

}
