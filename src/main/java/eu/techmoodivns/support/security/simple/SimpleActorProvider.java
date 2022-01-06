package eu.techmoodivns.support.security.simple;

import eu.techmoodivns.support.security.actor.ActorProvider;
import eu.techmoodivns.support.security.authenticator.Authenticator;

public class SimpleActorProvider implements ActorProvider {

    private String username;
    private String password;
    private String name;

    public SimpleActorProvider(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Authenticator.Actor match(Authenticator.Credentials credentials) {
        if (credentials.getPassword().equals(this.password)
                && credentials.getUsername().equals(this.username)) {
            return createActor();
        }

        return null;
    }

    @Override
    public Authenticator.Actor find(String id) {
        return createActor();
    }

    private Authenticator.Actor createActor() {
        Authenticator.Actor actor = new Authenticator.Actor();

        actor.setId("1");
        actor.setName(name != null ? name : username);

        return actor;
    }

}
