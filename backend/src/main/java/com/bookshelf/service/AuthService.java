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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtUtil               jwtUtil;
    private final OtpService            otpService;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    // ── Register ──────────────────────────────────────────────────────────────
    // saveNewUser() commits FIRST, then email is sent OUTSIDE the transaction.
    // This prevents the "transaction silently rolled back" error.
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "An account with this email already exists.");
        }

        // Commit user to DB in its own transaction before touching email
        saveNewUser(req);

        // Email runs AFTER commit — if it fails, user is already saved safely
        try {
            otpService.generateAndSend(req.getEmail());
        } catch (Exception e) {
            System.err.println("OTP email failed for " + req.getEmail() + ": " + e.getMessage());
        }
    }

    @Transactional
    public void saveNewUser(RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .authProvider("LOCAL")
                .build();
        userRepository.save(user);
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    @Transactional
    public void login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "No account found with this email."));

        if (user.getPasswordHash() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This account uses Google login. Please sign in with Google.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Incorrect password.");
        }

        try {
            otpService.generateAndSend(req.getEmail());
        } catch (Exception e) {
            System.err.println("OTP email failed for " + req.getEmail() + ": " + e.getMessage());
        }
    }

    // ── Verify OTP ────────────────────────────────────────────────────────────
    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest req) {
        boolean valid = otpService.verify(req.getEmail(), req.getCode());

        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid or expired OTP. Please try again.");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found."));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    }

    // ── Google OAuth ──────────────────────────────────────────────────────────
    @Transactional
    public AuthResponse googleLogin(GoogleAuthRequest req) throws Exception {
        GoogleIdToken idToken = googleIdTokenVerifier.verify(req.getIdToken());

        if (idToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid Google token.");
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
