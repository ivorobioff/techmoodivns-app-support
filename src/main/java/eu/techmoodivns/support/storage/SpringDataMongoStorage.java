package eu.techmoodivns.support.storage;

import org.javatuples.Pair;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;
import eu.techmoodivns.support.storage.Claim.Operator;
import static java.util.Optional.ofNullable;

public class SpringDataMongoStorage<T> implements EntityStorage<T> {

    private MongoTemplate mongoTemplate;

    private Class<T> type;

    public SpringDataMongoStorage(MongoTemplate mongoTemplate, Class<T> type) {
        this.mongoTemplate = mongoTemplate;
        this.type = type;
    }

    @Override
    public void store(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public Optional<T> find(List<Claim<?>> claims) {
        return ofNullable(mongoTemplate.findOne(toQuery(claims), type));
    }

    @Override
    public List<T> findAll(List<Claim<?>> claims) {
        return mongoTemplate.find(toQuery(claims), type);
    }

    @Override
    public void deleteAll(List<Claim<?>> claims) {
        mongoTemplate.remove(toQuery(claims), type);
    }

    private Query toQuery(List<Claim<?>> claims) {
        Query query = new Query();

        claims.forEach(claim -> query.addCriteria(toCriteria(claim)));

        return query;
    }

    private Criteria toCriteria(Claim<?> claim) {
        String property = claim.getProperty();
        Pair<Operator, ?> subject = claim.getSubject();
        Operator operator = subject.getValue0();
        Object value = subject.getValue1();

        Criteria criteria = Criteria.where(property);

        switch (operator) {
            case EQUAL:
                return criteria.is(value);
            case NOT_EQUAL:
                return criteria.ne(value);
            case LESS:
                return criteria.lt(value);
            case LESS_OR_EQUAL:
                return criteria.lte(value);
            case GREATER:
                return criteria.gt(value);
            case GREATER_OR_EQUAL:
                return criteria.gte(value);
            default:
                throw new IllegalStateException(operator.name() + " is not supported!");
        }
    }
}
