package eu.techmoodivns.support.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Objects;

import static eu.techmoodivns.support.random.RandomUtils.resolveValue;
import static eu.techmoodivns.support.random.RandomUtils.convertRawValue;
import static java.lang.String.format;

public class ObligatedValidator implements ConstraintValidator<Obligated, Object> {

    private String ifProperty;
    private String equals;
    private String requireProperty;

    private String message;

    @Override
    public void initialize(Obligated annotation) {
        ifProperty = annotation.ifProperty();
        equals = annotation.equals();
        requireProperty = annotation.requireProperty();
        message = annotation.message();
    }

    @Override
    public boolean isValid(Object source, ConstraintValidatorContext context) {

        if (source == null) {
            return true;
        }

        Object ifValue = resolveValue(source, ifProperty);
        Object requireValue = resolveValue(source, requireProperty);
        Object equalsValue = convertRawValue(source, ifProperty, equals);

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(format(message, ifProperty, equals))
            .addPropertyNode(requireProperty)
            .addConstraintViolation();

        if (!Objects.equals(ifValue, equalsValue)) {
            return true;
        }

        return notEmpty(requireValue);
    }

    private boolean notEmpty(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return ((String) value).trim().length() > 0;
        }

        if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        }

        return true;
    }
}
