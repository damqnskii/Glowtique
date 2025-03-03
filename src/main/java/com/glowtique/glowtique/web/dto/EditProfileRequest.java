package com.glowtique.glowtique.web.dto;

import com.glowtique.glowtique.user.model.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProfileRequest {
    @Size(max = 20, message = "First name can't have more than 20 symbols")
    private String firstName;

    @Size(max = 20, message = "Last name can't have more than 20 symbols")    private String lastName;

    @Email(message = "Must be email!")
    private String email;

    private String password;

    private String phoneNumber;

    private Country country;

    private String town;

    private String street;

    private String factureAddress;

    private String shippingAddress;
}
