package org.payd.services.implementations;

import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import org.payd.data.models.User;
import org.payd.data.repositories.UserRepository;
import org.payd.data.repositories.WalletBalanceRepository;
import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.requests.ForgetPasswordRequest;
import org.payd.dtos.requests.LoginUserRequest;
import org.payd.dtos.requests.ResetPasswordRequest;
import org.payd.dtos.responses.CreateUserResponse;
import org.payd.exceptions.UserNotFoundException;
import org.payd.mapper.Mapper;
import org.payd.services.interfaces.AccountService;
import org.payd.services.interfaces.AuthService;
import org.payd.services.interfaces.EmailService;
import org.payd.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final WalletBalanceRepository balanceRepository;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountService accountService, WalletBalanceRepository balanceRepository,  JwtUtils jwtUtils,  EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.balanceRepository = balanceRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    @Override
    public CreateUserResponse createAccount(CreateUserRequest request) throws Exception {
        if (userRepository.existsByEmail(request.email())) throw new IllegalArgumentException("Email already exists");
        User user = getUser(request);
        return Mapper.toCreateUserResponse(accountService.createWalletAndBalance(user), jwtUtils.generateTokenPair(user.getId()));
    }

    @Override
    public CreateUserResponse login(LoginUserRequest request) {
        User user = validateRequest(request);
        return Mapper.toCreateUserResponse(user, jwtUtils.generateTokenPair(user.getId()));
    }

    @Override
    public void forgotPassword(ForgetPasswordRequest request) {
        String token = jwtUtils.generateEmailToken(request.email());
        emailService.sendEmail(
                request.email(),
                "Reset your Payd password",
                passwordRecoveryEmail(token)
        );
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        if (!jwtUtils.isTokenValid(request.token())) return false;
        String email = jwtUtils.extractClaim(request.token(), Claims::getSubject);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user not found"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void deleteAllUsers() {
        balanceRepository.deleteAll();
        userRepository.deleteAll();
    }


    @NotNull
    private User validateRequest(LoginUserRequest request) {
        if (!userRepository.existsByEmail(request.email())) throw new IllegalArgumentException("Invalid email or password");
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) throw new IllegalArgumentException("Invalid email or password");
        return user;
    }

    @NotNull
    private User getUser(CreateUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .fullName(request.fullname())
                .password(hashedPassword)
                .build();
        return userRepository.save(user);
    }


    public String passwordRecoveryEmail(String token) {
        return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Password Recovery</title>
        <style>
            body {
                margin: 0;
                padding: 0;
                background-color: #f6f8fa;
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                color: #202124;
            }
            .container {
                max-width: 600px;
                margin: 40px auto;
                background: #ffffff;
                border-radius: 8px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                padding: 32px;
            }
            .logo {
                font-size: 22px;
                font-weight: 600;
                color: #1a73e8;
                margin-bottom: 24px;
            }
            h1 {
                font-size: 20px;
                margin-bottom: 16px;
                font-weight: 500;
            }
            p {
                font-size: 15px;
                line-height: 1.6;
                margin-bottom: 16px;
            }
            .button {
                display: inline-block;
                margin-top: 24px;
                padding: 12px 24px;
                background-color: #1a73e8;
                color: #ffffff;
                text-decoration: none;
                border-radius: 4px;
                font-weight: 500;
            }
            .footer {
                margin-top: 32px;
                font-size: 12px;
                color: #5f6368;
            }
            .divider {
                height: 1px;
                background-color: #e0e0e0;
                margin: 24px 0;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="logo">Payd</div>

            <h1>Password recovery</h1>

            <p>Hello,</p>

            <p>
                We received a request to reset your Payd account password.
                Use the token below to complete your password reset.
            </p>

            <div class="divider"></div>

            <p style="font-size:18px; font-weight:600;">
                """ + token + """
            </p>

            <p>
                This token will expire in <strong>15 minutes</strong>.
                If you did not request a password reset, you can safely ignore this email.
            </p>

            <p>
                Thanks,<br>
                <strong>The Payd Team</strong>
            </p>

            <div class="footer">
                © 2026 Payd. All rights reserved.
            </div>
        </div>
    </body>
    </html>
    """;
    }

}
