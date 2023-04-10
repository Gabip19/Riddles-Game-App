package riddler.domain.validator.exceptions;

public class ChallengeValidationException extends ValidationException {
    public ChallengeValidationException() {
        super();
    }

    public ChallengeValidationException(String message) {
        super(message);
    }

    public ChallengeValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChallengeValidationException(Throwable cause) {
        super(cause);
    }

    protected ChallengeValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
