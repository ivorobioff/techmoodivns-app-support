package eu.techmoodivns.support.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CorsProperties {

    @Value("${eu.techmoodivns.support.cors.path:}")
    private String path = "/api/**";

    @Value("${eu.techmoodivns.support.cors.allowedOrigins:}")
    private String[] allowedOrigins = new String[]{"*"};

    @Value("${eu.techmoodivns.support.cors.allowedMethods:}")
    private String[] allowedMethods = new String[]{"*"};

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
