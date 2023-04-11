package riddler.domain.validator.exceptions;

public class NoAttemptsLeftException extends SubmissionException {
    public NoAttemptsLeftException() {
        super();
    }

    public NoAttemptsLeftException(String message) {
        super(message);
    }

    public NoAttemptsLeftException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAttemptsLeftException(Throwable cause) {
        super(cause);
    }

    protected NoAttemptsLeftException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
