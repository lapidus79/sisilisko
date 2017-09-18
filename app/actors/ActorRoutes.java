package actors;


import java.util.Optional;

public class ActorRoutes {
    static final String ROOT_PATH = "user/";

    public static final String DASHBOARD_MANAGER_NAME = "dashboard-manager";
    public static final String DASHBOARD_MANAGER_PATH = ROOT_PATH.concat(DASHBOARD_MANAGER_NAME);

    public static final String CLIENT_MANAGER_NAME = "client-manager";
    public static final String CLIENT_MANAGER_PATH = ROOT_PATH.concat(CLIENT_MANAGER_NAME);

    public static final String DASHBOARD_DAO_NAME = "dashboard-deo-router";
    public static final String DASHBOARD_DAO_PATH = ROOT_PATH.concat(DASHBOARD_DAO_NAME);

    public static final String WIDGET_DAO_NAME = "widget-deo-router";
    public static final String WIDGET_DAO_PATH = ROOT_PATH.concat(WIDGET_DAO_NAME);

    public static final String DB_EXEC_CTX = "database.dispatcher";

    /**
     * Validate that the tokenId contains only valid actor name chars.
     * Otherwise traversing the akka path might be possible or other mischief
     */
    public static Optional<String> dashboardActorPath(final String tokenId) {
        if (tokenId != null && tokenId.matches("^[a-zA-Z0-9]{32}$") ){
            return Optional.of(DASHBOARD_MANAGER_PATH.concat("/").concat(tokenId));
        }
        return Optional.empty();
    }

    public static Optional<String> widgetActorPath(final String tokenId,
                                                   final Long id) {
        Optional<String> dbPath = dashboardActorPath(tokenId);
        if (dbPath.isPresent()) {
            return Optional.of(dbPath.get().concat("/").concat(id.toString()));
        }
        return Optional.empty();
    }

}
