package eu.techmoodivns.support.token;

import eu.techmoodivns.support.mongo.MongoConfiguration;
import eu.techmoodivns.support.storage.SpringDataMongoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan
@Configuration
@EnableScheduling
@Import(MongoConfiguration.class)
public class TokenConfiguration {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public TokenManager tokenManager() {
        return new TokenManager(new SpringDataMongoStorage<>(mongoTemplate, Token.class), tokenProperties);
    }
}
