package actors.widget.ping;


import akka.actor.Actor;

public class PingWorkerProtocol {

    public static class PingCmd {
        final String url;
        final long timeout;
        public PingCmd(String url, long timeout) {
            this.url = url;
            this.timeout = timeout;
        }
    }

    public static class PingEvt {
        public final Integer statusCode;
        public final Long timestamp;
        public final Long latency;
        public PingEvt(final Integer statusCode, final Long timestamp, final Long latency) {
            this.statusCode = statusCode;
            this.timestamp = timestamp;
            this.latency = latency;
        }
    }

    public interface Factory {
        Actor create();
    }
}
