package org.payd.services.implementations;

import jakarta.validation.constraints.NotNull;
import org.payd.data.models.User;
import org.payd.data.repositories.UserRepository;
import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.responses.CreateUserResponse;
import org.payd.mapper.Mapper;
import org.payd.services.interfaces.AccountService;
import org.payd.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Override
    public CreateUserResponse createAccount(CreateUserRequest request) throws Exception {
        User user = getUser(request);
        return Mapper.toCreateUserResponse(accountService.createWalletAndBalance(user));
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
}
