package actors.dao;


import akka.actor.Actor;
import models.Widget;
import models.widget.PingWidgetConf;

public class WidgetDAOProtocol {

    /**
     * Fetch Widget from storage
     */
    public static class Fetch {
        public final Long id;
        public Fetch(final Long id) {
            this.id = id;
        }
    }

    /**
     * Factory for building correct DAO out of Widget Model
     */
    public static WidgetDAO build(Widget w) {
        if (w.getType().equals(Widget.Type.PING)) {
            return new PingWidgetDAO(w);
        } else {
            throw new IllegalStateException("WidgetDAO could not be build for widget of type " + w.getType());
        }
    }

    /**
     * Widget DAO model
     */
    public abstract static class WidgetDAO {
        public final Long id;
        public final String name;
        public final String type;
        public final Long dashboardId;
        public final String dashboardTokenId;
        public WidgetDAO(final Widget widget) {
            this.id = widget.getId();
            this.type = widget.getType().toString();
            this.name = widget.getName();
            this.dashboardId = widget.getDashboard().getId();
            this.dashboardTokenId = widget.getDashboard().getTokenId();
        }
    }

    /**
     * DAO for Widget.Type.PING
     */
    public static class PingWidgetDAO extends WidgetDAO {
        public final String url;
        public final Integer interval;
        public PingWidgetDAO(final Widget widget) {
            super(widget);
            PingWidgetConf conf = (PingWidgetConf) widget.getWidgetConf();
            this.url = conf.getUrl();
            this.interval = conf.getInterval();
        }
    }

    /**
     * If any DAO operation fails we send this to ourselves
     * so that we can throw an exception from the actor and
     * initiate the supverision strategy
     */
    public static class Failed {
        public final String msg;
        public Failed(final String msg) {
            this.msg = msg;
        }
    }

    public interface Factory {
        public Actor create();
    }

}
