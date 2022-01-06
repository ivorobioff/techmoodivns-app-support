package eu.techmoodivns.support.security;

import eu.techmoodivns.support.security.authenticator.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static eu.techmoodivns.support.security.authenticator.Authenticator.*;

public class SecretAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private Authenticator authenticator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Resolution resolution = authenticator.login((String) authentication.getCredentials());

        if (resolution == null) {
            return null;
        }

        return new AuthenticatedAuthentication(resolution);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SecretAuthentication.class);
    }
}
