package org.payd.dtos.responses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserResponse(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Full name is required")
        String fullname,

        @NotBlank(message = "polygon address is required")
        String walletAddress
) {}
