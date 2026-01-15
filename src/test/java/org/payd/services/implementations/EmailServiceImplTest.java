package org.payd.services.implementations;

import org.junit.jupiter.api.Test;
import org.payd.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class EmailServiceImplTest {

    @Autowired
    private EmailService emailService;

    @Test
    void sendEmail() {
        emailService.sendEmail("divineobinali9@gmail.com", "PAYD EMAIL TESTING", "testing");
    }
}