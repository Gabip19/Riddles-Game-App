package riddler.domain.validator.exceptions;

import riddler.domain.User;
import riddler.domain.validator.Validator;


public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) throws UserValidationException {
        String errors = "";

        errors = validateName(errors, user.getFirstName(), user.getLastName());
        errors = validateEmail(errors, user.getEmail());

        if (!(errors.equals(""))) {
            throw new UserValidationException(errors);
        }
    }

    private static String validateName(String errors, String firstName, String lastName) {
        if (lastName.length() < 2 || lastName.length() > 30 ||
                firstName.length() < 2 || firstName.length() > 30) {
            errors += "Name must have between 2 and 30 characters.\n";
        }
        if (!lastName.matches("^[a-zA-Z].*") || !firstName.matches("^[a-zA-Z].*")) {
            errors += "Name must start with a letter.\n";
        }
        if (!lastName.matches("^[a-zA-Z0-9-' ]+$") || !firstName.matches("^[a-zA-Z0-9-' ]+$")) {
            errors += "Unexpected symbol in name.\n";
        }
        return errors;
    }

    private static String validateEmail(String errors, String email) {
        if (!email.matches("^[a-z][a-z0-9-_.]+@[a-z][a-z0-9-_.]*\\.[a-z]+$")) {
            errors += "Invalid email format.\n";
        }
        return errors;
    }

}
