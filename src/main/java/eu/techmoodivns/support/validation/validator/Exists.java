package eu.techmoodivns.support.validation.validator;

import org.springframework.data.repository.CrudRepository;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
public @interface Exists {
    String message() default "does not exist";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends CrudRepository<?, String>> repository();
}
