package eu.techmoodivns.support.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import eu.techmoodivns.support.security.authenticator.Authenticator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecretAuthenticationFilter extends OncePerRequestFilter {

    private String name;
    private Authenticator authenticator;

    public SecretAuthenticationFilter(Authenticator authenticator) {
        this("Secret-Token", authenticator);
    }

    public SecretAuthenticationFilter(String name, Authenticator authenticator) {
        this.name = name;
        this.authenticator = authenticator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String secret = request.getHeader(name);

        if (secret != null) {
            var securityContext = SecurityContextHolder.createEmptyContext();

            var resolution = authenticator.login(secret);

            if (resolution != null) {
                securityContext.setAuthentication(new AuthenticatedAuthentication(resolution));
                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request, response);
    }
}
