package eu.techmoodivns.support.random;

import java.time.LocalDateTime;
import java.util.Objects;

public class ComparisonUtils {

    public static boolean same(Object left, Object right) {
        verifyType(left, right);

        return Objects.equals(left, right);
    }

    public static boolean greater(Object left, Object right, boolean orEqual) {
        verifyType(left, right);

        if (left == null && right != null) {
            return false;
        }

        if (left == null && right == null) {
            return orEqual;
        }

        if (left != null && right == null) {
            return true;
        }

        if (left instanceof Integer) {
            return greaterInteger((Integer) left, (Integer) right, orEqual);
        }

        if (left instanceof Double) {
            return greaterDouble((Double) left, (Double) right, orEqual);
        }

        if (left instanceof LocalDateTime) {
            return greaterLocalDateTime((LocalDateTime) left, (LocalDateTime) right, orEqual);
        }

        throw new IllegalStateException(left.getClass().getName() + " is not supported");
    }

    public static boolean less(Object left, Object right, boolean orEqual) {
        verifyType(left, right);

        if (left == null && right != null) {
            return true;
        }

        if (left == null && right == null) {
            return orEqual;
        }

        if (left != null && right == null) {
            return false;
        }

        if (left instanceof Integer) {
            return lessInteger((Integer) left, (Integer) right, orEqual);
        }

        if (left instanceof Double) {
            return lessDouble((Double) left, (Double) right, orEqual);
        }

        if (left instanceof LocalDateTime) {
            return lessLocalDateTime((LocalDateTime) left, (LocalDateTime) right, orEqual);
        }

        throw new IllegalStateException(left.getClass().getName() + " is not supported");
    }

    public static boolean sameType(Object left, Object right) {

        if (right == null || left == null) {
            return true;
        }

        return left.getClass().equals(right.getClass());
    }

    private static void verifyType(Object left, Object right) {
        if (!sameType(left, right)) {
            throw new IllegalStateException("'left' and 'right' must be of the same class");
        }
    }

    private static boolean greaterInteger(Integer left, Integer right, boolean orEqual) {
        return orEqual ? left >= right : left > right;
    }

    private static boolean lessInteger(Integer left, Integer right, boolean orEqual) {
        return orEqual ? left <= right : left < right;
    }

    private static boolean greaterDouble(Double left, Double right, boolean orEqual) {
        return orEqual ? left >= right : left > right;
    }

    private static boolean lessDouble(Double left, Double right, boolean orEqual) {
        return orEqual ? left <= right : left < right;
    }

    private static boolean greaterLocalDateTime(LocalDateTime left, LocalDateTime right, boolean orEqual) {
        return left.isAfter(right) || (orEqual ? left.equals(right) : false);
    }

    private static boolean lessLocalDateTime(LocalDateTime left, LocalDateTime right, boolean orEqual) {
        return left.isBefore(right) || (orEqual ? left.equals(right) : false);
    }
}
