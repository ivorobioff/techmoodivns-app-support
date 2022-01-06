package eu.techmoodivns.support.security;

import static eu.techmoodivns.support.security.authenticator.Authenticator.*;

public class AuthenticatedAuthentication extends ConfigurableAuthentication {

    public AuthenticatedAuthentication(Resolution resolution) {
        this.setPrincipal(resolution.getActor());
        this.setCredentials(resolution.getSecret());
        this.setAuthenticated(true);
    }
}
