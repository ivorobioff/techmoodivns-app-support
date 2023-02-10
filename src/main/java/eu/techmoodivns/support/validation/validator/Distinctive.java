package eu.techmoodivns.support.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DistinctiveValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface Distinctive {
    String message() default "must contain unique elements";

    String byField();

    boolean caseInsensitive() default false;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
