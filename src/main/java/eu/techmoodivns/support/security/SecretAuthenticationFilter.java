package eu.techmoodivns.support.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecretAuthenticationFilter extends OncePerRequestFilter {

    private String name;

    public SecretAuthenticationFilter() {
        this("Secret-Token");
    }


    public SecretAuthenticationFilter(String name) {
        this.name = name;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String secret = request.getHeader(name);

        if (secret != null) {
            SecurityContextHolder.getContext().setAuthentication(new SecretAuthentication(secret));
        }

        filterChain.doFilter(request, response);
    }
}
