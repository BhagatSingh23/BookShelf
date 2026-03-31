package com.bookshelf.controller;

import com.bookshelf.dto.request.*;
import com.bookshelf.dto.response.AuthResponse;
import com.bookshelf.service.AuthService;
import com.bookshelf.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    // POST /api/auth/register
    // Body: { name, email, password }
    // Creates account and sends OTP to email
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok(Map.of("message", "Account created. Check your email for the OTP."));
    }

    // POST /api/auth/login
    // Body: { email, password }
    // Validates password, sends OTP to email
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        authService.login(req);
        return ResponseEntity.ok(Map.of("message", "OTP sent to your email."));
    }

    // POST /api/auth/send-otp
    // Body: { email }
    // Resend OTP without password (for Google users or resend flow)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody OtpSendRequest req) {
        otpService.generateAndSend(req.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent."));
    }

    // POST /api/auth/verify-otp
    // Body: { email, code }
    // Verifies OTP, returns JWT token
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest req) {
        return ResponseEntity.ok(authService.verifyOtp(req));
    }

    // POST /api/auth/google
    // Body: { idToken }  (Google ID token from frontend)
    // Returns JWT token
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@Valid @RequestBody GoogleAuthRequest req)
            throws Exception {
        return ResponseEntity.ok(authService.googleLogin(req));
    }
}
