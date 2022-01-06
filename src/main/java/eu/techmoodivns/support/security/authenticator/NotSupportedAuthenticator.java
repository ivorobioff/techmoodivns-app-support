package eu.techmoodivns.support.security.authenticator;

public class NotSupportedAuthenticator implements Authenticator {

    private static final String NOT_SUPPORTED_MESSAGE = "Auth not supported";

    @Override
    public Resolution login(String secret) {
        throw new IllegalStateException(NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public Resolution login(Credentials credentials) {
        throw new IllegalStateException(NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public Resolution refresh(String secret) {
        throw new IllegalStateException(NOT_SUPPORTED_MESSAGE);
    }

    @Override
    public void logout(String secret) {
        throw new IllegalStateException(NOT_SUPPORTED_MESSAGE);
    }
}
