package eu.techmoodivns.support.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class ValidationExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidPayloadException.class)
    @ResponseBody
    public Map<String, ?> handle(InvalidPayloadException ex, WebRequest request) {
        return ex.getFailure();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Map<String, ?> handle(MissingServletRequestParameterException ex, WebRequest request) {
        return Map.of(ex.getParameterName(), "must be present");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOperationException.class)
    @ResponseBody
    public Map<String, ?> handle(InvalidOperationException ex, WebRequest request) {
        if (ex.getCause() != null) {
            logger.error(ex.getMessage(), ex.getCause());
        }

        return ex.getFailure();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public Map<String, ?> handle(ResourceNotFoundException ex, WebRequest request) {
        return ex.getFailure();
    }
}
