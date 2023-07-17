package io.khamzin.likeinsta.validations;

import io.khamzin.likeinsta.annotation.PasswordMatches;
import io.khamzin.likeinsta.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest userSignupRequest = (SignupRequest) object;
        return userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword());
    }

}
