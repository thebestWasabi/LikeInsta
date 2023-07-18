package io.khamzin.likeinsta.validation;

import io.khamzin.likeinsta.annotation.PasswordMatches;
import io.khamzin.likeinsta.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) object;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }

}
