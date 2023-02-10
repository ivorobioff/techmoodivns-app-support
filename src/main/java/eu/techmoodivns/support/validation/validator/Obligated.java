package eu.techmoodivns.support.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ObligatedValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@Repeatable(Obligated.List.class)
public @interface Obligated {

    String message() default "must have value when '%s' equals '%s'";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String requireProperty();
    String ifProperty();
    String equals();

    @Target({ TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Obligated[] value();
    }
}
