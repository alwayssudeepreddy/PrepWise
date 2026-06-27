package com.prepwise.prepwise_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String Username;

    @NotBlank(message = "Full name is required")
    private String FullName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String Email;

    @NotBlank(message = "Password is required")
    private String Password;

}