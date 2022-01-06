package eu.techmoodivns.support.validation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ComparedValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@Repeatable(Compared.List.class)
public @interface Compared {

    String message() default "must be %s '%s'";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String left();
    String right();
    String condition() default "=";

    @Target({ TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Compared[] value();
    }
}
