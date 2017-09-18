
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    protected void configure() {
        bind(AppStartUp.class).asEagerSingleton();
    }
}


