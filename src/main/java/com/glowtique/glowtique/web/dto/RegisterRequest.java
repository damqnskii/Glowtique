package com.glowtique.glowtique.web.dto;

import com.glowtique.glowtique.user.model.UserGender;
import com.glowtique.glowtique.validation.PasswordMatchValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatchValidator(message = "Your password and confirmation password must match!")
public class RegisterRequest {
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 symbols")
    @NotNull(message = "Enter your first name")
    public String firstName;

    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 symbols")
    @NotNull(message = "Enter your last name")
    public String lastName;

    @Email(message = "Must be email!")
    public String email;

    public UserGender gender;

    @NotNull(message = "Select a birthday date!")
    public LocalDate birthday;

    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    @NotNull(message = "Enter a password")
    public String password;

    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
    @NotNull(message = "Confirm the password")
    public String confirmPassword;

}
