package eu.techmoodivns.support.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
