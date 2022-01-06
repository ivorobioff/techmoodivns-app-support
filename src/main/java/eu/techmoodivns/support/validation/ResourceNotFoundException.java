package eu.techmoodivns.support.validation;

public class ResourceNotFoundException extends FriendlyException {

    public ResourceNotFoundException() {
        super("Resource not found!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
