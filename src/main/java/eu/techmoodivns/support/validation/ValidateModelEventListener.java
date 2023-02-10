package eu.techmoodivns.support.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import static org.springframework.validation.ValidationUtils.invokeValidator;

@Component
@Order(500)
public class ValidateModelEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private Validator validator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {

        Object source = event.getSource();

        BindingResult errors = new BeanPropertyBindingResult(source, source.getClass().getSimpleName());

        invokeValidator(validator, source, errors);

        if (errors.hasErrors()) {
            throw new InvalidPayloadException(errors);
        }
    }
}
