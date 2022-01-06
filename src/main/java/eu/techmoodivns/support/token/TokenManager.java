package eu.techmoodivns.support.token;

import eu.techmoodivns.support.storage.EntityStorage;
import eu.techmoodivns.support.storage.Claim;

import java.time.ZoneOffset;
import java.util.UUID;
import static java.time.LocalDateTime.now;

import static eu.techmoodivns.support.storage.Claim.Operator.*;

public class TokenManager {

    private EntityStorage<Token> storage;

    private TokenProperties properties;

    public TokenManager(EntityStorage<Token> storage, TokenProperties properties) {
        this.storage = storage;
        this.properties = properties;
    }

    public Token generate(String intent) {
        return generate(intent, null);
    }

    public Token generate(String intent, Object data) {
        Token token = new Token();

        token.setGeneratedAt(now(ZoneOffset.UTC));
        token.setData(data);
        token.setIntent(intent);
        token.setExpiresAt(now(ZoneOffset.UTC).plusMinutes(properties.getLifetime()));

        token.setSecret(generateSecret());

        storage.store(token);

        return token;
    }

    public Token lookup(String secret, String intent) {
        return storage.find(
                new Claim<>("secret", EQUAL, secret),
                new Claim<>("intent", EQUAL, intent),
                new Claim<>("expiresAt", GREATER, now(ZoneOffset.UTC))
        ).orElse(null);
    }

    public void cleanUp() {
        storage.deleteAll(new Claim<>("expiresAt", LESS_OR_EQUAL, now(ZoneOffset.UTC)));
    }

    private String generateSecret() {
        return UUID.randomUUID().toString();
    }
}
