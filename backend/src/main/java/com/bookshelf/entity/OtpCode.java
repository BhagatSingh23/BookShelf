package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OtpCode {

    // To create a time constraint on the otp sent
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
