package riddler.domain.validator;

import riddler.domain.Challenge;
import riddler.domain.validator.exceptions.ChallengeValidationException;
import riddler.domain.validator.exceptions.ValidationException;

public class ChallengeValidator implements Validator<Challenge> {
    @Override
    public void validate(Challenge entity) throws ValidationException {
        String errors = "";

        if (entity.getMaxAttempts() != Challenge.INFINITE_ATTEMPTS && entity.getMaxAttempts() <= 0) {
            errors += "The number of attempts must be a positive integer.\n";
        }

        if (entity.getBadgesPrizePool() > entity.getAuthor().getNoBadges()) {
            errors += "You do not have enough badges.\n";
        }

        if (entity.getTokensPrizePool() > entity.getAuthor().getNoTokens()) {
            errors += "You do not have enough tokens.\n";
        }

        if (entity.getTokensPrize() > entity.getTokensPrizePool()) {
            errors += "Number of prize tokens must be lower than the token pool.\n";
        }

        if (!(errors.equals(""))) {
            throw new ChallengeValidationException(errors);
        }
    }
}
