package eu.techmoodivns.support.token;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("tokens")
public class Token {

    @Id
    private String id;
    private Object data;
    private String secret;
    private String intent;
    private LocalDateTime expiresAt;

    @CreatedDate
    private LocalDateTime generatedAt;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getSecret() {
        return secret;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getIntent() {
        return intent;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
