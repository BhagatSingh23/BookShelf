package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // Unique id for the user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User data
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String passwordHash;

    @Column(nullable = false)
    private String authProvider = "LOCAL"; // "LOCAL" or "GOOGLE"

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastLogin;
}
