package com.unsia.edu.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email must be filled")
    private String email;
    @NotBlank(message = "Password must be filled")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
