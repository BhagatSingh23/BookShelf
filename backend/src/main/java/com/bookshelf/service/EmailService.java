package com.bookshelf.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp (String email, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your One-Time Password (OTP)");
            message.setText(buildEmailText(code));
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending OTP email to " + email, e);
        }
    }

    private String buildEmailText(String otp) {
        return """
        📚 Bookshelf

        Your OTP is: %s

        This code expires in 10 minutes.
        Do not share it with anyone.
        """.formatted(otp);
    }
}