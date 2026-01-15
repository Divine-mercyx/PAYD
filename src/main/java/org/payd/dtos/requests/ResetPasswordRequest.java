package org.payd.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "token is required")
        String token,
        @NotBlank(message = "password is required")
        String newPassword
) {}
