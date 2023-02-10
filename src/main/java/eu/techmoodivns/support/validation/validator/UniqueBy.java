package eu.techmoodivns.support.validation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import eu.techmoodivns.support.validation.validator.UniqueByValidator.Qualifier;
import eu.techmoodivns.support.validation.validator.UniqueByValidator.NullQualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueByValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@Repeatable(UniqueBy.Many.class)
public @interface UniqueBy {
    String message() default "";

    boolean caseInsensitive() default false;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String[] value();

    String id() default "id";

    Class<? extends Qualifier> qualifier() default NullQualifier.class;

    @Target({ TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface Many {
        UniqueBy[] value();
    }
}
