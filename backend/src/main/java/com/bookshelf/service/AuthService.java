package com.bookshelf.service;

import com.bookshelf.dto.request.GoogleAuthRequest;
import com.bookshelf.dto.request.LoginRequest;
import com.bookshelf.dto.request.OtpVerifyRequest;
import com.bookshelf.dto.request.RegisterRequest;
import com.bookshelf.dto.response.AuthResponse;
import com.bookshelf.entity.User;
import com.bookshelf.repository.UserRepository;
import com.bookshelf.security.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier; // injected from GoogleOAuthConfig

    // ── Register (manual) ────────────────────────────────────
    @Transactional
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .authProvider("LOCAL")
                .build();
        userRepository.save(user);
        otpService.generateAndSend(req.getEmail());
    }

    // ── Login step 1: validate password, send OTP ────────────
    @Transactional
    public void login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        otpService.generateAndSend(req.getEmail());
    }

    // ── Login step 2: verify OTP, return JWT ─────────────────
    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest req) {
        boolean valid = otpService.verify(req.getEmail(), req.getCode());
        if (!valid) {
            throw new RuntimeException("Invalid or expired OTP");
        }
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    }

    // ── Google OAuth ─────────────────────────────────────────
    @Transactional
    public AuthResponse googleLogin(GoogleAuthRequest req) throws Exception {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(req.getIdToken());
        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name  = (String) payload.get("name");

        User user = userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .authProvider("GOOGLE")
                        .build())
        );

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user.getName(), user.getEmail());
    }
}
