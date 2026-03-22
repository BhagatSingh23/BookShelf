package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {

    // Unique id to the added book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lazy fetch to get limited info
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Info on the book
    private String olBookId;      // Open Library work ID e.g. "OL45883W"
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(length = 300)
    private String author;
    
    @Column(length = 200)
    private String genre;
    
    private Integer totalPages = 0;
    
    @Column(length = 500)
    private String coverUrl;
    
    @Column(length = 500)
    private String olUrl;         // Direct link to read on Open Library

    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt = LocalDateTime.now();
}
