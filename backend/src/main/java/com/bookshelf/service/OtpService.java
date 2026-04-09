package com.bookshelf.service;

import com.bookshelf.entity.OtpCode;
import com.bookshelf.repository.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final EmailService emailService;

    @Value("${otp.expiry.minutes}")
    private int expiryMinutes;

    @Transactional
    public void generateAndSend(String email) {
        // Invalidate any existing OTPs for this email
        otpCodeRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());

        String code = String.format("%06d", new Random().nextInt(999999));

        OtpCode otp = OtpCode.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(expiryMinutes))
                .used(false)
                .build();

        otpCodeRepository.save(otp);
        emailService.sentOtp(email, code);
    }

    @Transactional
    public boolean verify(String email, String code) {
        OtpCode otp = otpCodeRepository
                .findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
                        email, LocalDateTime.now())
                .orElse(null);

        if (otp == null || !otp.getCode().equals(code)) {
            return false;
        }

        otp.setUsed(true);
        otpCodeRepository.save(otp);
        return true;
    }
}
