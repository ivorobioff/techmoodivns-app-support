package eu.techmoodivns.support.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProperties {

    @Value("${eu.techmoodivns.support.token.lifetime:60}")
    private Integer lifetime; // minutes

    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }

    public Integer getLifetime() {
        return lifetime;
    }
}
