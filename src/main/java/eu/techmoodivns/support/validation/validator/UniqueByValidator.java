package eu.techmoodivns.support.validation.validator;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static eu.techmoodivns.support.random.RandomUtils.resolveValue;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

public class UniqueByValidator implements ConstraintValidator<UniqueBy, Object> {

    private MongoTemplate mongoTemplate;
    private AutowireCapableBeanFactory beanFactory;

    private Qualifier qualifier;

    private boolean caseInsensitive = false;

    private String idField;
    private List<String> fields;

    public UniqueByValidator(MongoTemplate mongoTemplate, AutowireCapableBeanFactory beanFactory) {
        this.mongoTemplate = mongoTemplate;
        this.beanFactory = beanFactory;
    }

    @Override
    public void initialize(UniqueBy annotation) {

        fields = Arrays.asList(annotation.value());
        idField = annotation.id();
        caseInsensitive = annotation.caseInsensitive();

        Class<? extends Qualifier> qualifierType = annotation.qualifier();

        if (qualifierType != NullQualifier.class) {
            qualifier = beanFactory.createBean(qualifierType);
        }
    }

    @Override
    public boolean isValid(Object source, ConstraintValidatorContext context) {

        if (source == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        if (fields.size() == 1) {
            context.buildConstraintViolationWithTemplate("must be unique")
                    .addPropertyNode(fields.get(0))
                    .addConstraintViolation();
        } else {
            context.buildConstraintViolationWithTemplate(
                    String.format("must be unique by '%s'", String.join("', '", fields)));
        }

        Map<String, String> targets = new HashMap<>();

        fields.forEach(field -> {
            targets.put(field, (String) resolveValue(source, field));
        });

        String id = (String) resolveValue(source, idField);

        return !exists(source, targets, id);
    }

    private boolean exists(Object source, Map<String, String> targets, String id) {

        Query q = new Query();

        targets.forEach((field, value) -> {

            if (value != null) {
                if (caseInsensitive) {
                    q.addCriteria(Criteria.where(field)
                            .regex("^" + Pattern.quote(value.trim()) + "$", "i"));
                } else {
                    q.addCriteria(Criteria.where(field).is(value.trim()));
                }
            } else {
                q.addCriteria(Criteria.where(field).is(null));
            }
        });

        if (qualifier != null) {
            qualifier.getCriteria().forEach(q::addCriteria);
        }

        if (id != null) {
            q.addCriteria(Criteria.where(idField).ne(id));
        }

        return mongoTemplate.exists(q, source.getClass());
    }

    public interface Qualifier {
        List<CriteriaDefinition> getCriteria();
    }

    public static class NullQualifier implements Qualifier {

        @Override
        public List<CriteriaDefinition> getCriteria() {
            return emptyList();
        }
    }
}
