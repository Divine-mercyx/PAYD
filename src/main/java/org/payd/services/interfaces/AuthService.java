package org.payd.services.interfaces;

import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.responses.CreateUserResponse;

public interface AuthService {

    CreateUserResponse createAccount(CreateUserRequest request) throws Exception;
}
