package org.payd.services.interfaces;


public interface EmailService {

    void sendEmail(String email, String subject, String body);
}
