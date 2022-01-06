package eu.techmoodivns.support.data;

import eu.techmoodivns.support.data.annotation.Default;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;

import static eu.techmoodivns.support.random.RandomUtils.resolveValue;
import static eu.techmoodivns.support.random.RandomUtils.assignValue;
import static eu.techmoodivns.support.random.RandomUtils.convertRawValue;
import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

@Component
@Order(510)
@SuppressWarnings("unchecked")
public class AssignDefaultEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {

        Object source = event.getSource();

        Set<Field> fields = getAllFields(source.getClass(), withAnnotation(Default.class));

        fields.forEach(field -> {
            if (resolveValue(source, field) == null) {
                assignValue(source, field, resolveDefaultValue(source, field));
            }
        });
    }

    private Object resolveDefaultValue(Object source, Field field) {
        Default annotation = field.getAnnotation(Default.class);
        String rawValue = annotation.value();

        return convertRawValue(source, field, rawValue);
    }
}
