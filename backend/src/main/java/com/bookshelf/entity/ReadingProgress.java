package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

// This will help us to store the reading progress of the user in the database
@Entity
@Table(name = "reading_progress")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadingProgress {

    // To track the progress of the user on the particular book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    @Column(nullable = false)
    private Integer pagesRead = 0;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    private LocalDateTime lastUpdated = LocalDateTime.now();
}
