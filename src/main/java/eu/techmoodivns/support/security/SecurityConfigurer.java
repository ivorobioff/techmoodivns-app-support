package eu.techmoodivns.support.security;

import eu.techmoodivns.support.cors.CorsConfiguration;
import eu.techmoodivns.support.security.preference.SecurityPreference;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import static eu.techmoodivns.support.security.controller.AuthController.*;
import static java.util.stream.Collectors.*;

@Component
public class SecurityConfigurer {

    @Bean
    WebSecurityCustomizer configureWebSecurity(SecurityPreference securityPreference) throws Exception {
        return (WebSecurity web) -> {
            var openEndpoints = securityPreference.getOpenEndpoints()
                    .stream()
                    .collect(groupingBy(SecurityPreference.Endpoint::getMethod,
                            mapping(SecurityPreference.Endpoint::getPath, toSet())));

            var ignoring = web.ignoring();

            ignoring.requestMatchers(
                    LOGIN_ENDPOINT,
                    LOGOUT_ENDPOINT,
                    REFRESH_ENDPOINT);

            openEndpoints.forEach((method, paths) -> ignoring.requestMatchers(method, paths.toArray(new String[0])));
        };
    }

    @Bean
    SecurityFilterChain configureSecurityFilterChain(
            HttpSecurity http,
            ApplicationContext applicationContext) throws Exception {

        http.securityMatcher("/api/v1.0/**");

        var authenticationProvider = applicationContext
                .getAutowireCapableBeanFactory()
                .createBean(SecretAuthenticationProvider.class);

        http.authenticationProvider(authenticationProvider);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterAfter(new SecretAuthenticationFilter(), BasicAuthenticationFilter.class);

        if (applicationContext.getBeanNamesForType(CorsConfiguration.class).length > 0) {
            http.cors();
        }

        http.authorizeHttpRequests().requestMatchers("/**").authenticated();

        http.csrf().disable();

        return http.build();
    }
}
