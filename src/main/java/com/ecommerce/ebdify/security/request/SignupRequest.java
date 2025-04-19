package com.ecommerce.ebdify.security.request;

import java.util.Set;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40, message = "Password must be of at least 8 characters")
    private String password;

    @Getter
    @Setter
    private Set<String> role;

}