package com.bookshelf.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sentOtp (String toEmail, String otp) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Bookshelf <onboarding@resend.dev>")
                .to(toEmail)
                .subject("Your Bookshelf OTP Code")
                .html(buildEmailHtml(otp))
                .build();

        try {
            resend.emails().send(params);
            System.out.println("OTP email sent to: " + toEmail);
        } catch (ResendException e) {
            System.err.println("Resend email failed for " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    private String buildEmailHtml(String otp) {
        return """
                <div style="font-family:sans-serif;max-width:480px;margin:0 auto;padding:32px;
                            background:#f9fafb;border-radius:12px;">
                  <h2 style="color:#4f46e5;margin-bottom:8px;">📚 Bookshelf</h2>
                  <p style="color:#374151;font-size:16px;">Your one-time password is:</p>
                  <div style="font-size:40px;font-weight:700;letter-spacing:12px;
                              color:#111827;margin:24px 0;text-align:center;">
                    %s
                  </div>
                  <p style="color:#6b7280;font-size:14px;">
                    This code expires in 10 minutes. Do not share it with anyone.
                  </p>
                </div>
                """.formatted(otp);
    }
}