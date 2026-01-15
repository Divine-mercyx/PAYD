package org.payd.services.interfaces;

import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.requests.ForgetPasswordRequest;
import org.payd.dtos.requests.LoginUserRequest;
import org.payd.dtos.requests.ResetPasswordRequest;
import org.payd.dtos.responses.CreateUserResponse;

public interface AuthService {

    CreateUserResponse createAccount(CreateUserRequest request) throws Exception;
    void deleteAllUsers();
    CreateUserResponse login(LoginUserRequest request);
    void forgotPassword(ForgetPasswordRequest request);
    boolean resetPassword(ResetPasswordRequest request);
}
