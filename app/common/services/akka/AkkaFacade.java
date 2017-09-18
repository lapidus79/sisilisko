package common.services.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.Optional;

abstract class AkkaFacade {

    private final ActorSystem actorSystem;

    AkkaFacade(final ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    ActorRef lookup(final String path) {
        return actorSystem.actorFor(path);
    }

    String buildPath(final Optional<String> path) {
        return path.orElseThrow(() -> new IllegalArgumentException("invalid token path"));
    }
}

