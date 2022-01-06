package eu.techmoodivns.support.security;

public class SecretAuthentication extends ConfigurableAuthentication {

    public SecretAuthentication(String secret) {
        this.setCredentials(secret);
        this.setAuthenticated(false);
    }
}
