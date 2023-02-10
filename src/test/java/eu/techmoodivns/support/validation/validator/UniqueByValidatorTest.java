package eu.techmoodivns.support.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import jakarta.validation.ConstraintValidatorContext;
import static jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import static jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import jakarta.validation.Payload;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UniqueByValidatorTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private AutowireCapableBeanFactory beanFactory;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder builder;

    @Mock
    private NodeBuilderCustomizableContext customizableContext;

    private Query exists;

    private String message;
    private String propertyName;

    @BeforeEach
    public void setup() {
        message = null;
        propertyName = null;
        exists = null;

        lenient().when(context.buildConstraintViolationWithTemplate(anyString())).thenAnswer(a -> {
            message = a.getArgument(0);
            return builder;
        });

        lenient().when(builder.addPropertyNode(anyString())).thenAnswer(a -> {
            propertyName = a.getArgument(0);
            return customizableContext;
        });

        lenient().when(mongoTemplate.exists(any(Query.class), any(Class.class)))
                .thenAnswer(a -> {
                    Query query = a.getArgument(0);
                    return exists.equals(query);
                });

        lenient().when(beanFactory.createBean(eq(SimpleQualifier.class)))
                .thenReturn(new SimpleQualifier());
    }

    @Test
    public void testWithOneField() {
        UniqueByValidator validator = createValidator(createAnnotation("model"));

        exists = new Query()
                .addCriteria(Criteria.where("model").is("ford"));

        assertTrue(validator.isValid(new Car("audi"), context));

        // found
        assertFalse(validator.isValid(new Car("ford"), context));

        assertEquals("must be unique", message);
        assertEquals("model", propertyName);
    }

    @Test
    public void testWithManyFields() {
        UniqueByValidator validator = createValidator(createAnnotation(List.of("model", "tag")));

        exists = new Query()
                .addCriteria(Criteria.where("model").is("ford"))
                .addCriteria(Criteria.where("tag").is("b"));

        assertTrue(validator.isValid(new Car("ford", "a"), context));

        // found
        assertFalse(validator.isValid(new Car("ford", "b"), context));

        assertEquals("must be unique by 'model', 'tag'", message);
        assertNull(propertyName);
    }

    @Test
    public void testWithFieldNull() {
        UniqueByValidator validator = createValidator(createAnnotation(List.of("model", "tag")));

        exists = new Query()
                .addCriteria(Criteria.where("model").is("ford"))
                .addCriteria(Criteria.where("tag").is(null));

        assertTrue(validator.isValid(new Car("ford", "a"), context));

        // found
        assertFalse(validator.isValid(new Car("ford"), context));

        assertEquals("must be unique by 'model', 'tag'", message);
        assertNull(propertyName);
    }


    @Test
    public void testWithQualifier() {
        UniqueByValidator validator = createValidator(createAnnotation("model", SimpleQualifier.class));

        exists = new Query()
                .addCriteria(Criteria.where("model").is("ford"))
                .addCriteria(Criteria.where("price").is("2"));

        assertTrue(validator.isValid(new Car("audi"), context));

        // found
        assertFalse(validator.isValid(new Car("ford"), context));

        assertEquals("must be unique", message);
        assertEquals("model", propertyName);
    }

    @Test
    public void testWithId() {
        UniqueByValidator validator = createValidator(createAnnotation("model"));

        exists = new Query()
                .addCriteria(Criteria.where("id").ne("2"))
                .addCriteria(Criteria.where("model").is("ford"));

        Car car1 = new Car("ford");
        car1.id = "1";

        assertTrue(validator.isValid(car1, context));

        Car car2 = new Car("ford");
        car2.id = "2";

        // found
        assertFalse(validator.isValid(car2, context));

        assertEquals("must be unique", message);
        assertEquals("model", propertyName);
    }

    @Test
    public void testWithAnotherId() {
        UniqueByValidator validator = createValidator(createAnnotation("model", "tag"));

        exists = new Query()
                .addCriteria(Criteria.where("tag").ne("2"))
                .addCriteria(Criteria.where("model").is("ford"));

        assertTrue(validator.isValid(new Car("ford", "1"), context));

        // found
        assertFalse(validator.isValid(new Car("ford", "2"), context));

        assertEquals("must be unique", message);
        assertEquals("model", propertyName);
    }


    @Test
    public void testWithCaseInsensitive() {
        UniqueByValidator validator = createValidator(createAnnotation("model", true));

        exists = new Query()
                .addCriteria(Criteria.where("model")
                        .regex("^" + Pattern.quote("ford") + "$", "i"));

        assertTrue(validator.isValid(new Car("audi"), context));

        // found
        assertFalse(validator.isValid(new Car("ford"), context));
    }

    private UniqueByValidator createValidator(UniqueBy annotation) {
        UniqueByValidator validator = new UniqueByValidator(mongoTemplate, beanFactory);

        validator.initialize(annotation);

        return validator;
    }

    public UniqueBy createAnnotation(String field, Class<? extends UniqueByValidator.Qualifier> qualifier) {
        return createAnnotation(List.of(field), false, qualifier, null);
    }

    public UniqueBy createAnnotation(String field, String id) {
        return createAnnotation(List.of(field), false, null, id);
    }

    public UniqueBy createAnnotation(String field, boolean caseInsensitive) {
        return createAnnotation(List.of(field), caseInsensitive, null, null);
    }

    public UniqueBy createAnnotation(String field) {
        return createAnnotation(List.of(field), false, null, null);
    }

    public UniqueBy createAnnotation(List<String> fields) {
        return createAnnotation(fields, false, null, null);
    }

    public UniqueBy createAnnotation(
            List<String> fields,
            boolean caseInsensitive,
            Class<? extends UniqueByValidator.Qualifier> qualifier,
            String id
    ) {

        return new UniqueBy() {
            @Override
            public String message() {
                return null;
            }

            @Override
            public boolean caseInsensitive() {
                return caseInsensitive;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String[] value() {
                return fields.toArray(new String[]{});
            }

            @Override
            public String id() {
                return id == null ? "id" : id;
            }

            @Override
            public Class<? extends UniqueByValidator.Qualifier> qualifier() {
                return qualifier;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return UniqueBy.class;
            }
        };
    }

    private static class SimpleQualifier implements UniqueByValidator.Qualifier {
        @Override
        public List<CriteriaDefinition> getCriteria() {
            return List.of(Criteria.where("price").is("2"));
        }
    }

    private static class Car {
        private String id;
        private String model;
        private String tag;

        public Car(String model, String tag) {
            this.model = model;
            this.tag = tag;
        }

        public Car(String model) {
            this.model = model;
        }
    }
}
