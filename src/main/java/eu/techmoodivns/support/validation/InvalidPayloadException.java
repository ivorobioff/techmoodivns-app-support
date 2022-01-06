package eu.techmoodivns.support.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class InvalidPayloadException extends FriendlyException {

    public InvalidPayloadException(Errors errors) {
        this(parseErrors(errors));
    }

    public InvalidPayloadException(Map<String, String> errors) {
        super(Map.of("errors", errors));
    }

    public static Map<String, String> parseErrors(Errors errors) {
        Map<String, String> errorsMap = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {

            if (!errorsMap.containsKey(error.getField())) {
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
        }

        return errorsMap;
    }
}
