package eu.techmoodivns.support.storage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EntityStorage<T> {
    void store(T entity);
    Optional<T> find(List<Claim<?>> claims);
    List<T> findAll(List<Claim<?>> claims);
    void deleteAll(List<Claim<?>> claims);

    default Optional<T> find(Claim<?>...claims) {
        return find(Arrays.asList(claims));
    }

    default List<T> findAll(Claim<?>...claims) {
        return findAll(Arrays.asList(claims));
    }

    default void deleteAll(Claim<?>...claims) {
        deleteAll(Arrays.asList(claims));
    }
}
