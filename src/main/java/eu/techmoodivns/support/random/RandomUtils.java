package eu.techmoodivns.support.random;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.springframework.util.ReflectionUtils.findField;
import static java.util.Collections.emptySet;

public class RandomUtils {

    private static final Map<Class<?>, Function<String, ?>> rawValueConverters = Map.of(
            Boolean.class, Boolean::parseBoolean,
            Integer.class, Integer::parseInt,
            Double.class, Double::parseDouble,
            String.class, v -> v
    );

    public static Object convertRawValue(Object source, String property, String rawValue) {
        Field field = findField(source.getClass(), property);

        if (field == null) {
            throw new RuntimeException(fieldToString(source, property) + " does not exist!");
        }

        return convertRawValue(source, field, rawValue);
    }

    public static Object convertRawValue(Object source, Field field, String rawValue) {
        Class<?> expectedType = field.getType();

        if (!rawValueConverters.containsKey(expectedType)) {
            throw new RuntimeException("Unable to resolve default value for " + fieldToString(source, field));
        }

        return rawValueConverters.get(expectedType).apply(rawValue);
    }

    public static void assignValue(Object source, String property, Object value) {
        Field field = findField(source.getClass(), property);

        if (field == null) {
            throw new RuntimeException(fieldToString(source, property) + " does not exist!");
        }

        assignValue(source, field, value);
    }

    public static void assignValue(Object source, Field field, Object value) {
        field.setAccessible(true);

        try {
            field.set(source, value);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to write " + fieldToString(source, field));
        } finally {
            field.setAccessible(false);
        }
    }

    public static Object resolveValue(Object source, String property) {
        Field field = findField(source.getClass(), property);

        if (field == null) {
            throw new RuntimeException(fieldToString(source, property) + " does not exist!");
        }

        return resolveValue(source, field);
    }

    public static Object resolveValue(Object source, Field field) {
        field.setAccessible(true);

        try {
            return field.get(source);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to read " + fieldToString(source, field));
        } finally {
            field.setAccessible(false);
        }
    }

    public static String fieldToString(Object source, Field field) {
        return fieldToString(source, field.getName());
    }

    public static String fieldToString(Object source, String field) {
        return source.getClass().getName() + "$" + field;
    }

    public static <T>void transportProperties(T from, T to) {
        transportProperties(from, to, emptySet());
    }

    @SuppressWarnings("unchecked")
    public static <T>void transportProperties(T from, T to, Set<String> excludes) {

        if(!from.getClass().isAssignableFrom(to.getClass())) {
            throw new IllegalStateException("The `from` class must be either the same as, "
                    + "or is a superclass of the `to` class!");
        }

        Predicate<Field> predicate = field -> !excludes.contains(field.getName())
                && resolveValue(from, field) != null;

        Consumer<Field> action = fromField ->
                assignValue(to, fromField.getName(), resolveValue(from, fromField));

        getAllFields(from.getClass(), predicate).forEach(action);
    }
}
