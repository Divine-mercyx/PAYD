package org.payd.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Full name is required")
        String fullname,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String password
) {}
