package actors.client;

import akka.actor.ActorRef;

public class ClientManagerProtocol {

    public static class TerminateClient {
        public final ActorRef client;
        public TerminateClient(ActorRef client) {
            this.client = client;
        }
    }

    public static class CreateClient {
        public String id;
        public ActorRef out;

        public CreateClient(String id, ActorRef out) {
            this.id = id;
            this.out = out;
        }
    }

}
