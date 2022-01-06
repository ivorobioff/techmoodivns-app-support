package eu.techmoodivns.support.validation.validator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsValidator implements ConstraintValidator<Exists, String> {

    @Autowired
    private BeanFactory beanFactory;

    private CrudRepository<?, String> repository;

    @Override
    public void initialize(Exists annotation) {
        repository = beanFactory.getBean(annotation.repository());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.existsById(value);
    }
}
