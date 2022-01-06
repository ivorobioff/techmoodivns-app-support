package eu.techmoodivns.support.security;

import eu.techmoodivns.support.security.authenticator.Authenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProperties {

    @Value("${eu.techmoodivns.support.security.authenticator:}")
    private Class<? extends Authenticator> authenticator;

    public void setAuthenticator(Class<? extends Authenticator> authenticator) {
        this.authenticator = authenticator;
    }

    public Class<? extends Authenticator> getAuthenticator() {
        return authenticator;
    }
}
