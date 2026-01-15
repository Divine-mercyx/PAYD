package org.payd.controllers;

import jakarta.validation.Valid;
import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.requests.ForgetPasswordRequest;
import org.payd.dtos.requests.LoginUserRequest;
import org.payd.dtos.requests.ResetPasswordRequest;
import org.payd.dtos.responses.CreateUserResponse;
import org.payd.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/createAccount")
    public ResponseEntity<CreateUserResponse> createAccount(@Valid @RequestBody CreateUserRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createAccount(request));
    }

    @PostMapping("/login")
    public ResponseEntity<CreateUserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("token sent to your email");
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?>  resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws Exception {
        boolean success = authService.resetPassword(request);
        if (success) return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid or expired token"));
    }
}
