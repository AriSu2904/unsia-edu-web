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
public class RegisterRequest {
    @Size(min = 3, message = "First name must be at least 3 characters")
    private String firstName;
    @Size(min = 3, message = "Last name must be at least 3 characters")
    private String lastName;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email must be filled")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @Size(min = 10, message = "Phone number must be at least 10 characters")
    private String phoneNumber;
}
