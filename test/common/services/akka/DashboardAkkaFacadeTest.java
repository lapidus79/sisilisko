package common.services.akka;

import actors.dashboard.DashboardActorProtocol;
import actors.dashboard.DashboardManagerActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestProbe;
import models.Dashboard;
import org.junit.Before;
import org.junit.Test;

import static akka.testkit.JavaTestKit.duration;
import static org.mockito.Mockito.*;

    public class DashboardAkkaFacadeTest {

    String dashboardTokenId = "TjuEC3WcGVgMALvLtjzaCfgJQeXBaPfg";
    String invalidToken = "../../dadasadasas";
    Dashboard dashboard = new Dashboard();
    DashboardAkkaFacade dashboardAkkaFacade;
    ActorSystem system;
    ActorSystem systemReal;

    @Before
    public void setUp() {
        system = mock(ActorSystem.class);
        systemReal = ActorSystem.create();
        dashboardAkkaFacade = new DashboardAkkaFacade(system);
    }

    @Test
    public void addDashboard() {
        dashboard.setTokenId(dashboardTokenId);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager")).thenReturn(mock);
        dashboardAkkaFacade.addDashboard(dashboard);
        probe.expectMsgClass(duration("1 second"), DashboardManagerActorProtocol.StartDashboard.class);
    }

    @Test
    public void terminateDashboard() {
        dashboard.setTokenId(dashboardTokenId);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager")).thenReturn(mock);
        dashboardAkkaFacade.terminateDashboard(dashboard);
        probe.expectMsgClass(duration("1 second"), DashboardManagerActorProtocol.TerminateDashboard.class);
    }

    @Test
    public void refreshDashboard() {
        dashboard.setTokenId(dashboardTokenId);
        final TestProbe probe = TestProbe.apply(systemReal);
        final ActorRef mock = probe.ref();
        when(system.actorFor("user/dashboard-manager/" + dashboardTokenId)).thenReturn(mock);
        dashboardAkkaFacade.refreshDashboard(dashboard);
        probe.expectMsgClass(duration("1 second"), DashboardActorProtocol.LoadDashboardCmd.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refreshDashboard_whenDashboardTokenIdIsInvalid() {
        dashboard.setTokenId(invalidToken);
        dashboardAkkaFacade.refreshDashboard(dashboard);
    }

}
