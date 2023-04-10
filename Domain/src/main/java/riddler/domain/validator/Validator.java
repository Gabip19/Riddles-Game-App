package riddler.domain.validator;


import riddler.domain.validator.exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

