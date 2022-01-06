package eu.techmoodivns.support.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public abstract class ConfigurableAuthentication implements Authentication {
    private Collection<? extends GrantedAuthority> authorities;

    private Object credentials;

    private Object details;

    private Object principal;

    private boolean isAuthenticated;

    private String name;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    protected void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    protected void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
