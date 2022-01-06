package eu.techmoodivns.support.security.simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleSecurityProperties {

    @Value("${eu.techmoodivns.support.security.simple.actor.username}")
    private String actorUsername;

    @Value("${eu.techmoodivns.support.security.simple.actor.password}")
    private String actorPassword;

    @Value("${eu.techmoodivns.support.security.simple.actor.name:}")
    private String actorName;

    public String getActorUsername() {
        return actorUsername;
    }

    public void setActorUsername(String actorUsername) {
        this.actorUsername = actorUsername;
    }

    public String getActorPassword() {
        return actorPassword;
    }

    public void setActorPassword(String actorPassword) {
        this.actorPassword = actorPassword;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }
}
