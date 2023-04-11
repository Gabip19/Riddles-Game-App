package riddler.domain.validator.exceptions;

public class SubmissionException extends RuntimeException {
    public SubmissionException() {
        super();
    }

    public SubmissionException(String message) {
        super(message);
    }

    public SubmissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubmissionException(Throwable cause) {
        super(cause);
    }

    protected SubmissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
