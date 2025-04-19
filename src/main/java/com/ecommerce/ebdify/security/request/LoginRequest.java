package com.ecommerce.ebdify.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be of at least 8 characters")
    private String password;
}
