package eu.techmoodivns.support.security.simple;

import eu.techmoodivns.support.security.SecurityConfiguration;
import eu.techmoodivns.support.security.actor.ActorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
@Import({ SecurityConfiguration.class })
public class SimpleSecurityConfiguration {

    @Bean
    public ActorProvider simpleActorProvider(SimpleSecurityProperties properties) {

        SimpleActorProvider actorProvider = new SimpleActorProvider(
                properties.getActorUsername(),
                properties.getActorPassword()
        );

        actorProvider.setName(properties.getActorName());

        return actorProvider;
    }
}
