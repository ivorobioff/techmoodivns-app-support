package eu.techmoodivns.support.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import eu.techmoodivns.support.storage.Claim.Operator;
import org.javatuples.Pair;

import static java.util.stream.Collectors.toList;
import static eu.techmoodivns.support.random.RandomUtils.*;
import static eu.techmoodivns.support.random.ComparisonUtils.*;

public class InMemoryStorage<T> implements EntityStorage<T> {

    private List<T> data = new ArrayList<>();

    @Override
    public void store(T entity) {
        data.add(entity);
    }

    @Override
    public Optional<T> find(List<Claim<?>> claims) {
        return data.stream()
                .filter(toPredicate(claims))
                .findFirst();
    }

    @Override
    public List<T> findAll(List<Claim<?>> claims) {
        return data.stream()
                .filter(toPredicate(claims))
                .collect(toList());
    }

    @Override
    public void deleteAll(List<Claim<?>> claims) {
        data = data.stream()
                .filter(toPredicate(claims).negate())
                .collect(toList());
    }

    private Predicate<T> toPredicate(List<Claim<?>> claims) {
        return entity -> toPredicates(claims).stream().allMatch(p -> p.test(entity));
    }

    private List<Predicate<T>> toPredicates(List<Claim<?>> claims) {
        return claims.stream().map(claim -> (Predicate<T>) entity -> {

            Pair<Operator, ?> subject = claim.getSubject();

            String property = claim.getProperty();
            Object leftValue = resolveValue(entity, property);
            Operator operator = subject.getValue0();
            Object rightValue = subject.getValue1();

            switch (operator) {
                case EQUAL:
                    return same(leftValue, rightValue);
                case NOT_EQUAL:
                    return !same(leftValue, rightValue);
                case LESS:
                    return less(leftValue, rightValue, false);
                case LESS_OR_EQUAL:
                    return less(leftValue, rightValue, true);
                case GREATER:
                    return greater(leftValue, rightValue, false);
                case GREATER_OR_EQUAL:
                    return greater(leftValue, rightValue, true);
                default: throw new IllegalStateException(operator.name() + " is not supported!");
            }
        }).collect(Collectors.toList());
    }
}
