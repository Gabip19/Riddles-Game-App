package riddler.domain.validator.exceptions;

public class InvalidSubmissionAnswerException extends SubmissionException {
    public InvalidSubmissionAnswerException() {
        super();
    }

    public InvalidSubmissionAnswerException(String message) {
        super(message);
    }

    public InvalidSubmissionAnswerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSubmissionAnswerException(Throwable cause) {
        super(cause);
    }

    protected InvalidSubmissionAnswerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
