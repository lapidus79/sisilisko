import com.google.inject.Inject;
import common.seed.Seed;

public class AppStartUp {

    @Inject
    public AppStartUp() {
        boolean dropAndSeed = Boolean.parseBoolean(System.getProperty("dropAndSeed"));
        if (dropAndSeed) {
            Seed.dropAndSeedDb();
        }
    }

}
