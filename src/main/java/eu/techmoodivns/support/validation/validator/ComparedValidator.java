package eu.techmoodivns.support.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static eu.techmoodivns.support.random.RandomUtils.resolveValue;
import static java.lang.String.format;

public class ComparedValidator implements ConstraintValidator<Compared, Object> {

    private Map<String, ComparatorContext> comparators = createComparators(
            new ComparatorContext(">", "greater than", createGreaterComparators()),
            new ComparatorContext(">=", "greater than, or equal to", createGreaterOrEqualComparators()),
            new ComparatorContext("<", "less than", createLessComparators()),
            new ComparatorContext("<=", "less than, or equal to", createLessOrEqualComparators()),
            new ComparatorContext("==", "equal to", createEqualComparators()),
            new ComparatorContext("!=", "not equal to", createNotEqualComparators())
    );

    private String left;
    private String right;
    private String condition;

    private String message;

    @Override
    public void initialize(Compared annotation) {
        left = annotation.left();
        right = annotation.right();
        condition = annotation.condition();
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object source, ConstraintValidatorContext context) {
        if (source == null) {
            return true;
        }

        return compare(resolveValue(source, left), resolveValue(source, right), context);
    }

    @SuppressWarnings("unchecked")
    private <T>boolean compare(T leftValue, T rightValue, ConstraintValidatorContext context) {

        if (leftValue == null || rightValue == null) {
            return true;
        }

        Class<T> leftType = (Class<T>)leftValue.getClass();
        Class<T> rightType = (Class<T>)rightValue.getClass();

        if (!leftType.equals(rightType)) {
            throw new IllegalStateException("Left and right values must be of the exact same type");
        }

        if (!comparators.containsKey(condition)) {
            throw new RuntimeException(condition + " is not supported");
        }

        ComparatorContext comparatorContext = comparators.get(condition);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(format(message, comparatorContext.getTitle(), right))
                .addPropertyNode(left)
                .addConstraintViolation();


        Comparator<T> comparator = (Comparator<T>)comparatorContext.getComparators().get(leftType);

        if (comparator == null) {
            throw new RuntimeException(leftType.getName() + " is not supported");
        }

        return comparator.compare(leftValue, rightValue);
    }

    private static Map<String, ComparatorContext> createComparators(ComparatorContext ...contexts) {
        return Arrays.stream(contexts).collect(
                toMap(ComparatorContext::getName, c -> c));
    }

    private interface Comparator<T> {
        boolean compare(T left, T right);
    }

    private static Map<Class<?>, Comparator<?>> createGreaterComparators() {
        return Map.of(
                Double.class,
                (Comparator<Double>) (left, right) -> left > right,

                Integer.class,
                (Comparator<Integer>) (left, right) -> left > right,

                Float.class,
                (Comparator<Float>) (left, right) -> left > right
        );
    }

    private static Map<Class<?>, Comparator<?>> createGreaterOrEqualComparators() {
        return Map.of(
                Double.class,
                (Comparator<Double>) (left, right) -> left >= right,

                Integer.class,
                (Comparator<Integer>) (left, right) -> left >= right,

                Float.class,
                (Comparator<Float>) (left, right) -> left >= right
        );
    }

    private static Map<Class<?>, Comparator<?>> createLessComparators() {
        return Map.of(
                Double.class,
                (Comparator<Double>) (left, right) -> left < right,

                Integer.class,
                (Comparator<Integer>) (left, right) -> left < right,

                Float.class,
                (Comparator<Float>) (left, right) -> left < right
        );
    }

    private static Map<Class<?>, Comparator<?>> createLessOrEqualComparators() {
        return Map.of(
                Double.class,
                (Comparator<Double>) (left, right) -> left <= right,

                Integer.class,
                (Comparator<Integer>) (left, right) -> left <= right,

                Float.class,
                (Comparator<Float>) (left, right) -> left <= right
        );
    }

    private static Map<Class<?>, Comparator<?>> createEqualComparators() {
        return Map.of(
                Double.class,
                (Comparator<Double>) Double::equals,

                Integer.class,
                (Comparator<Integer>) Integer::equals,

                Float.class,
                (Comparator<Float>) Float::equals
        );
    }

    private static Map<Class<?>, Comparator<?>> createNotEqualComparators() {
        return Map.of(

                Double.class,
                (Comparator<Double>) (left, right) -> !left.equals(right),

                Integer.class,
                (Comparator<Integer>) (left, right) -> !left.equals(right),

                Float.class,
                (Comparator<Float>) (left, right) -> !left.equals(right)
        );
    }

    private static class ComparatorContext {
        private String title;
        private String name;
        private Map<Class<?>, Comparator<?>> comparators;

        public ComparatorContext(
                String name,
                String title,
                Map<Class<?>, Comparator<?>> comparators
        ) {
            this.name = name;
            this.title = title;
            this.comparators = comparators;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setComparators(Map<Class<?>, Comparator<?>> comparators) {
            this.comparators = comparators;
        }

        public Map<Class<?>, Comparator<?>> getComparators() {
            return comparators;
        }
    }
}
