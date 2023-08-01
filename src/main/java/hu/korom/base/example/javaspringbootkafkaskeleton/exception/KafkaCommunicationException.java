package hu.korom.base.example.javaspringbootkafkaskeleton.exception;

/**
 * A custom exception type especially for this microservice.
 * The exception is extended with an error code field.
 */
public class KafkaCommunicationException extends RuntimeException {

    private final String errorCode;

    public KafkaCommunicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
