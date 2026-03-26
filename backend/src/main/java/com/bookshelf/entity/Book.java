package com.bookshelf.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String olBookId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 300)
    private String author;

    @Column(length = 200)
    private String genre;

    @Builder.Default
    private Integer totalPages = 0;

    @Column(length = 500)
    private String coverUrl;

    @Column(length = 500)
    private String olUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime addedAt;
}
