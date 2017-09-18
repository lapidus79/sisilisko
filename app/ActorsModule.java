import actors.ActorRoutes;
import actors.client.ClientActor;
import actors.client.ClientActorProtocol;
import actors.client.ClientManagerActor;
import actors.dao.DashboardDAOActor;
import actors.dao.DashboardDAOProtocol;
import actors.dao.WidgetDAOActor;
import actors.dao.WidgetDAOProtocol;
import actors.dashboard.DashboardActor;
import actors.dashboard.DashboardActorProtocol;
import actors.dashboard.DashboardManagerActor;
import actors.dashboard.InitDashboardManager;
import actors.widget.ping.PingWidgetActor;
import actors.widget.ping.PingWidgetProtocol;
import actors.widget.ping.PingWorkerActor;
import actors.widget.ping.PingWorkerProtocol;
import akka.routing.RoundRobinPool;
import com.google.inject.AbstractModule;
import org.apache.commons.lang3.Validate;
import play.Configuration;
import play.Environment;
import play.libs.akka.AkkaGuiceSupport;

public class ActorsModule extends AbstractModule implements AkkaGuiceSupport {

    public static final String DBOARD_POOL_SIZE = "sisilisko.routers.dashboard-dao-poolsize";
    public static final String WIDGET_POOL_SIZE = "sisilisko.routers.widget-dao-poolsize";


    private final Configuration configuration;

    public ActorsModule(Environment environment, Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {
        Integer dashboardPoolSize = configuration.getInt(DBOARD_POOL_SIZE);
        Integer widgetPoolSize = configuration.getInt(WIDGET_POOL_SIZE);
        Validate.notNull(dashboardPoolSize, DBOARD_POOL_SIZE.concat(" must be defined"));
        Validate.notNull(widgetPoolSize, WIDGET_POOL_SIZE.concat(" must be defined"));

        bindActor(ClientManagerActor.class, ActorRoutes.CLIENT_MANAGER_NAME);
        bindActor(DashboardManagerActor.class, ActorRoutes.DASHBOARD_MANAGER_NAME);

        bindActor(DashboardDAOActor.class, ActorRoutes.DASHBOARD_DAO_NAME, p -> new RoundRobinPool(dashboardPoolSize).props(p).withDispatcher(ActorRoutes.DB_EXEC_CTX));
        bindActor(WidgetDAOActor.class, ActorRoutes.WIDGET_DAO_NAME, p -> new RoundRobinPool(widgetPoolSize).props(p).withDispatcher(ActorRoutes.DB_EXEC_CTX));

        bindActorFactory(DashboardActor.class, DashboardActorProtocol.Factory.class);
        bindActorFactory(PingWidgetActor.class, PingWidgetProtocol.Factory.class);
        bindActorFactory(PingWorkerActor.class, PingWorkerProtocol.Factory.class);
        bindActorFactory(ClientActor.class, ClientActorProtocol.Factory.class);
        bindActorFactory(WidgetDAOActor.class, WidgetDAOProtocol.Factory.class);
        bindActorFactory(DashboardDAOActor.class, DashboardDAOProtocol.Factory.class);

        bind(InitDashboardManager.class).asEagerSingleton();
    }

}
