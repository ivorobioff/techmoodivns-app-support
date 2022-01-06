package eu.techmoodivns.support.security;

import eu.techmoodivns.support.cors.CorsConfiguration;
import eu.techmoodivns.support.security.preference.SecurityPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static eu.techmoodivns.support.security.controller.AuthController.*;
import static java.util.stream.Collectors.*;

@Component
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityPreference securityPreference;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        SecretAuthenticationProvider provider = getApplicationContext()
                .getAutowireCapableBeanFactory()
                .createBean(SecretAuthenticationProvider.class);

        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(WebSecurity web) {
        Map<HttpMethod, Set<String>> openEndpoints = securityPreference.getOpenEndpoints()
                .stream()
                .collect(groupingBy(SecurityPreference.Endpoint::getMethod, mapping(SecurityPreference.Endpoint::getPath, toSet())));


        WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();

        ignoring.mvcMatchers(
                LOGIN_ENDPOINT,
                LOGOUT_ENDPOINT,
                REFRESH_ENDPOINT);


        openEndpoints.forEach((method, paths) ->
                ignoring.mvcMatchers(method, paths.toArray(new String[0])));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.requestMatcher(new AntPathRequestMatcher("/api/v1.0/**"));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterAfter(new SecretAuthenticationFilter(), BasicAuthenticationFilter.class);

        if (applicationContext.getBeanNamesForType(CorsConfiguration.class).length > 0) {
            http.cors();
        }

        http.authorizeRequests().antMatchers("/**").authenticated();

        http.csrf().disable();
    }
}
