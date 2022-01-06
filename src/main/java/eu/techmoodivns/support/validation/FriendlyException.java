package eu.techmoodivns.support.validation;

import java.util.Map;

public class FriendlyException extends RuntimeException {

    private Map<String, ?> failure;

    public FriendlyException(Map<String, ?> failure) {
        this.failure = failure;
    }

    public FriendlyException(String message) {
        super(message);
        failure = toFailure(message);
    }

    public FriendlyException(String message, Throwable cause) {
        super(message, cause);
        failure = toFailure(message);
    }

    public Map<String, ?> getFailure() {
        return failure;
    }

    public Map<String, ?> toFailure(String message) {
        return Map.of("message", message);
    }
}
