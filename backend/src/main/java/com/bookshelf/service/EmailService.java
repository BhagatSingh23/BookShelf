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
            message.setText(buildEmailHtml(code));
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending OTP email to " + email, e);
        }
    }

    private String buildEmailHtml(String otp) {
        return """
        <html>
        <body style="margin:0;padding:0;background-color:#f9fafb;font-family:Arial,sans-serif;">
          <table align="center" width="100%%" cellpadding="0" cellspacing="0" style="max-width:480px;margin:40px auto;background:#ffffff;border-radius:10px;padding:20px;">
            
            <tr>
              <td style="text-align:center;">
                <h2 style="color:#4f46e5;margin-bottom:10px;">Bookshelf</h2>
              </td>
            </tr>

            <tr>
              <td style="text-align:center;font-size:16px;color:#374151;">
                Your one-time password is:
              </td>
            </tr>

            <tr>
              <td style="text-align:center;font-size:36px;font-weight:bold;letter-spacing:8px;color:#111827;padding:20px 0;">
                %s
              </td>
            </tr>

            <tr>
              <td style="text-align:center;font-size:14px;color:#6b7280;">
                This code expires in 10 minutes.<br/>
                Do not share it with anyone.
              </td>
            </tr>

          </table>
        </body>
        </html>
        """.formatted(otp);
    }
}