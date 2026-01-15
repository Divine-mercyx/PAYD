package org.payd.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgetPasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email
) {}
