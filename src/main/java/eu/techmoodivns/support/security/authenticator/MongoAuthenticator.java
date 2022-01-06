package eu.techmoodivns.support.security.authenticator;

import eu.techmoodivns.support.security.actor.ActorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import eu.techmoodivns.support.security.authenticator.Authenticator.Cleanable;

public class MongoAuthenticator implements Authenticator, Cleanable {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ActorProvider actorProvider;

    @Override
    public Resolution login(String secret) {

        Resolution resolution = mongoTemplate.findOne(defineQuery(secret), Resolution.class);

        if (resolution == null || isExpired(resolution)) {
            return null;
        }

        return merge(resolution);
    }

    @Override
    public Resolution login(Credentials credentials) {
        Actor actor = actorProvider.match(credentials);

        if (actor == null) {
            return null;
        }

        Resolution resolution = new Resolution();

        resolution.setActorId(actor.getId());
        resolution.setExpiresAt(getExpirationDate(credentials.getRemember() ? RefreshTerm.LONG : RefreshTerm.NORMAL));
        resolution.setSecret(generateSecret());

        mongoTemplate.save(resolution);

        return merge(resolution);
    }

    @Override
    public Resolution refresh(String secret) {
        Resolution resolution = mongoTemplate.findOne(defineQuery(secret), Resolution.class);

        if (resolution == null || isExpired(resolution)) {
            return null;
        }

        resolution.setSecret(generateSecret());
        resolution.setExpiresAt(getExpirationDate(RefreshTerm.SHORT));

        mongoTemplate.save(resolution);

        return merge(resolution);
    }

    @Override
    public void logout(String secret) {
        mongoTemplate.remove(defineQuery(secret), Resolution.class);
    }

    private Query defineQuery(String secret) {
        return new Query()
                .addCriteria(Criteria.where("secret").is(secret));
    }

    private Resolution merge(Resolution resolution) {

        Actor actor = actorProvider.find(resolution.getActorId());

        if (actor == null) {
            throw new IllegalStateException("Actor is missing!");
        }

        resolution.setActor(actor);

        return resolution;
    }

    private String generateSecret() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime getExpirationDate(RefreshTerm term) {

        LocalDateTime today = now(ZoneOffset.UTC);

        if (term.equals(RefreshTerm.LONG)) {
            return today.plusYears(1);
        }

        if (term.equals(RefreshTerm.NORMAL)) {
            return today.plusDays(1);
        }

        if (term.equals(RefreshTerm.SHORT)) {
            return today.plusHours(1);
        }

        throw new IllegalStateException(term.name() + " is not supported!");
    }

    private boolean isExpired(Resolution resolution) {
        return resolution.getExpiresAt().isBefore(now(ZoneOffset.UTC));
    }

    @Override
    public void cleanUp() {

        Criteria criteria = Criteria.where("expiresAt").lte(now(ZoneOffset.UTC));

        mongoTemplate.remove(new Query().addCriteria(criteria), Resolution.class);
    }

    enum RefreshTerm {
        SHORT, NORMAL, LONG
    }
}
