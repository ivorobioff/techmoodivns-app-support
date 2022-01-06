package eu.techmoodivns.support.token;

import eu.techmoodivns.support.storage.InMemoryStorage;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class TokenManagerTest {

    @Test
    public void testGenerate() {
        TokenManager tokenManager = new TokenManager(new InMemoryStorage<>(), createProperties());

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        Token token = tokenManager.generate("password-reset", "1");

        assertEquals("1", token.getData());
        assertEquals("password-reset", token.getIntent());
        assertNotNull(token.getSecret());

        assertEquals(now.getYear(), token.getGeneratedAt().getYear());
        assertEquals(now.getMonthValue(), token.getGeneratedAt().getMonthValue());
        assertEquals(now.getDayOfMonth(), token.getGeneratedAt().getDayOfMonth());
        assertEquals(now.getHour(), token.getGeneratedAt().getHour());

        assertEquals(now.getYear(), token.getExpiresAt().getYear());
        assertEquals(now.getMonthValue(), token.getExpiresAt().getMonthValue());
        assertEquals(now.getDayOfMonth(), token.getExpiresAt().getDayOfMonth());
        assertEquals(now.getHour() + 1, token.getExpiresAt().getHour());
    }

    @Test
    public void testGetValid() {
        TokenManager tokenManager = new TokenManager(new InMemoryStorage<>(), createProperties());

        Token validToken = tokenManager.generate("password-reset", "1");
        Token invalidToken = tokenManager.generate("password-reset", "1");
        invalidToken.setExpiresAt(invalidToken.getExpiresAt().minusMinutes(61));

        // correct secret and intent
        assertNotNull(tokenManager.lookup(validToken.getSecret(), "password-reset"));

        //wrong intent
        assertNull(tokenManager.lookup(validToken.getSecret(), "random"));

        // wrong expiresAt
        assertNull(tokenManager.lookup(invalidToken.getSecret(), "password-reset"));

        //wrong secret
        assertNull(tokenManager.lookup("a", "password-reset"));
    }

    @Test
    public void testCleanUp() {
        InMemoryStorage<Token> tokenStorage = new InMemoryStorage<>();

        TokenManager tokenManager = new TokenManager(tokenStorage, createProperties());

        tokenManager.generate("password-reset", "1");

        Token invalidToken = tokenManager.generate("password-reset", "1");
        invalidToken.setExpiresAt(invalidToken.getExpiresAt().minusMinutes(61));

        assertEquals(2, tokenStorage.findAll().size());

        tokenManager.cleanUp();

        assertEquals(1, tokenStorage.findAll().size());
    }


    private TokenProperties createProperties() {
        TokenProperties properties = new TokenProperties();

        properties.setLifetime(60);

        return properties;
    }
}
