package org.payd.services.implementations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.payd.dtos.requests.CreateUserRequest;
import org.payd.dtos.requests.LoginUserRequest;
import org.payd.dtos.responses.CreateUserResponse;
import org.payd.services.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

//    @BeforeEach
//    void setUp() {
//        authService.deleteAllUsers();
//    }

//    @AfterEach
//    void tearDown() {
//        authService.deleteAllUsers();
//    }

    @Test
    @Commit
    void createAccount() throws Exception {
        CreateUserRequest request = new CreateUserRequest("divineobinali@gmail.com", "divine mercy", "Aa11@abcdef");
        CreateUserResponse response = authService.createAccount(request);
        assertNotNull(response);
        assertNotNull(response.walletAddress());
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
    }


    @Test
    void testThatCreateAccountWillThrowErrorRegisteringWithExistingEmail() throws Exception {
        CreateUserRequest request = new CreateUserRequest("divine@gmail.com", "divine mercy", "Aa11@abcdef");
        CreateUserResponse response = authService.createAccount(request);
        assertNotNull(response);
        assertThrows(IllegalArgumentException.class, () -> authService.createAccount(request));
    }

    @Test
    void testThatUserCanLogin() throws Exception {
        CreateUserRequest request = new CreateUserRequest("divine@gmail.com", "divine mercy", "Aa11@abcdef");
        CreateUserResponse response = authService.createAccount(request);
        assertNotNull(response);

        LoginUserRequest loginUserRequest = new LoginUserRequest("divine@gmail.com", "Aa11@abcdef");
        CreateUserResponse loginUserResponse = authService.login(loginUserRequest);
        assertNotNull(loginUserResponse);
        assertNotNull(loginUserResponse.walletAddress());
        assertNotNull(loginUserResponse.accessToken());
        assertNotNull(loginUserResponse.refreshToken());
    }


    @Test
    void testThatUserCantLoginWithInvalidEmailOrPassword() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest("divine@gmail.com", "Aa11@abcde");
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginUserRequest));

        CreateUserRequest request = new CreateUserRequest("divine@gmail.com", "divine mercy", "Aa11@abcdef");
        CreateUserResponse response = authService.createAccount(request);
        assertNotNull(response);


        assertThrows(IllegalArgumentException.class, () -> authService.login(loginUserRequest));

    }
}