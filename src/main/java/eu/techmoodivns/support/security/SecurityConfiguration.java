package eu.techmoodivns.support.security;

import eu.techmoodivns.support.mongo.MongoConfiguration;
import eu.techmoodivns.support.random.RandomConfiguration;
import eu.techmoodivns.support.security.authenticator.Authenticator;
import eu.techmoodivns.support.security.authenticator.MongoAuthenticator;
import eu.techmoodivns.support.security.preference.SecurityPreference;
import eu.techmoodivns.support.security.preference.SecurityPreferenceCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;

import static java.util.Collections.emptyList;

@Configuration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "eu\\.techmoodivns\\.support\\.security\\.simple\\..*")
})
@EnableWebSecurity
@Import({RandomConfiguration.class, MongoConfiguration.class})
public class SecurityConfiguration {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private List<SecurityPreferenceCollector> securityPreferenceCollectors = emptyList();

    @Bean
    public Authenticator authenticator() {
        Class<? extends Authenticator> authenticatorType = securityProperties.getAuthenticator();

        if (authenticatorType == null) {
            return autowireCapableBeanFactory
                    .createBean(MongoAuthenticator.class);
        }

        return autowireCapableBeanFactory.createBean(authenticatorType);
    }

    @Bean
    public SecurityPreference securityPreference() {
        SecurityPreference preference = new SecurityPreference();

        securityPreferenceCollectors.forEach(collector -> collector.collect(preference));

        return preference;
    }
}
