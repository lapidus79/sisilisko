package actors.widget.ping;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.inject.Inject;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;

public class PingWorkerActor extends AbstractActor {

    public static final int STATUS_CONF_INVALID = -10;
    public static final int STATUS_HOST_UNKNOWN = -11;
    public static final int STATUS_REQUEST_TIMEOUT = -12;
    public static final int STATUS_CONNECTION_FAILED = -13;
    public static final int STATUS_FAILED_UNKNOWN = -20;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final WSClient wsClient;

    @Inject
    public PingWorkerActor(final WSClient wsClient) {
        this.wsClient = wsClient;
    }

    public static Props getProps() {
        return Props.create(PingWorkerActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PingWorkerProtocol.PingCmd.class, (msg) -> ping(msg))
                .build();
    }

    private void ping(PingWorkerProtocol.PingCmd cmd) {
        log.info("[{}].ping(): uri=[{}]", self().path().name(), cmd.url);
        long start = System.currentTimeMillis();
        ActorRef sender = getSender();
        try {
            wsClient.url(cmd.url)
                    .setRequestTimeout(java.time.Duration.of(cmd.timeout, ChronoUnit.MILLIS))
                    .get()
                    .whenComplete((r, t) -> handleResponse(r, t, sender, start));
        } catch (Exception e) {
            if (e.getCause() instanceof  MalformedURLException) {
                PingWorkerProtocol.PingEvt evt = new PingWorkerProtocol.PingEvt(STATUS_CONF_INVALID , start, -1L);
                sender.tell(evt, getSelf());
            } else {
                throw e;
            }
        }
    }

    private void handleResponse(WSResponse r, Throwable t, ActorRef sender, long start) {
        long latency = System.currentTimeMillis() - start;
        if (t != null) {
            mailPingEvt(getStatusCode(t), start, latency, sender);
        } else {
            mailPingEvt(r.getStatus(), start, latency, sender);
        }
    }

    private void mailPingEvt(int status, long start, long latency, ActorRef sender) {
        PingWorkerProtocol.PingEvt evt = new PingWorkerProtocol.PingEvt(status, start, latency);
        sender.tell(evt, getSelf());
    }

    private int getStatusCode(Throwable t) {
        Throwable cause = t.getCause();
        if (cause != null) {
            if (cause instanceof ConnectException) {
                return STATUS_CONNECTION_FAILED;
            } else if (cause instanceof TimeoutException) {
                return STATUS_REQUEST_TIMEOUT;
            } else if (cause instanceof UnknownHostException) {
                return STATUS_HOST_UNKNOWN;
            }
        }
        return STATUS_FAILED_UNKNOWN;
    }

}

