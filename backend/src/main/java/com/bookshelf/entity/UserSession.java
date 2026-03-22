package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserSession {

    // To keep the track of user session and throw him out after certain time of inactivation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 512)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
