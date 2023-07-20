package io.khamzin.likeinsta.payload.request;

import io.khamzin.likeinsta.annotation.PasswordMatches;
import io.khamzin.likeinsta.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @NotBlank(message = "Введите электронную почту")
    @ValidEmail
    private String email;

    @NotBlank(message = "Введите имя")
    private String firstname;

    @NotBlank(message = "Введите фамилию")
    private String lastname;

    @NotBlank(message = "Введите username")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 23, message = "Длинна пароля должна быть от 6 до 32 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirmPassword;

}
