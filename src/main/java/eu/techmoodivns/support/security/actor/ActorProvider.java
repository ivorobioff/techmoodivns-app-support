package eu.techmoodivns.support.security.actor;

import eu.techmoodivns.support.security.authenticator.Authenticator.Actor;
import eu.techmoodivns.support.security.authenticator.Authenticator.Credentials;

public interface ActorProvider {
    Actor match(Credentials credentials);
    Actor find(String id);
}
