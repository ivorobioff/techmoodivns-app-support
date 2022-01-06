package eu.techmoodivns.support.validation;

public class InvalidOperationException extends FriendlyException {

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
