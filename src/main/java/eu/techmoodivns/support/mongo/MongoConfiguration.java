package eu.techmoodivns.support.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@ComponentScan
@EnableMongoAuditing
public class MongoConfiguration {

    @Bean
    public MongoCustomConversions mongoUsualConversions() {
        return MongoCustomConversions.create(a -> a.useNativeDriverJavaTimeCodecs(true));
    }
}
