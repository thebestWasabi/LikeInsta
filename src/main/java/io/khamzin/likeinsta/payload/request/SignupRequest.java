package io.khamzin.likeinsta.payload.request;

import io.khamzin.likeinsta.annotation.PasswordMatches;
import io.khamzin.likeinsta.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "Incorrect email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Enter you name")
    private String firstname;

    @NotEmpty(message = "Enter you lastname")
    private String lastname;

    @NotEmpty(message = "Enter you username")
    private String username;

    @NotEmpty(message = "Enter you password")
    @Size(min = 6, max = 23, message = "Password must be between 6 and 23 symbols")
    private String password;

    private String confirmPassword;
}
