package eu.techmoodivns.support.security.authenticator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public interface Authenticator {
    Resolution login(String secret);
    Resolution login(Credentials credentials);
    Resolution refresh(String secret);
    void logout(String secret);

    @Document("auth")
    class Resolution {
        @Id
        private String id;

        private String actorId;

        @Transient
        private Actor actor;

        private LocalDateTime expiresAt;

        private String secret;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActorId() {
            return actorId;
        }

        public void setActorId(String actorId) {
            this.actorId = actorId;
        }

        public Actor getActor() {
            return actor;
        }

        public void setActor(Actor actor) {
            this.actor = actor;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getSecret() {
            return secret;
        }
    }

    class Actor {
        private String id;

        //displayable name
        private String name;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    class Credentials {
        @NotBlank
        private String username;

        @NotBlank
        private String password;

        private boolean remember = false;

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public void setRemember(boolean remember) {
            this.remember = remember;
        }

        public boolean getRemember() {
            return this.remember;
        }
    }

    interface Cleanable {
        void cleanUp();
    }
}
