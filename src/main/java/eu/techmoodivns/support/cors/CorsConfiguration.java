package eu.techmoodivns.support.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
public class CorsConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        CorsProperties corsProperties = applicationContext.getBean(CorsProperties.class);

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(corsProperties.getPath())
                        .allowedMethods(corsProperties.getAllowedMethods())
                        .allowedOrigins(corsProperties.getAllowedOrigins());
            }
        };
    }
}
