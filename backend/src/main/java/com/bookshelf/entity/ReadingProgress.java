package com.bookshelf.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_progress")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    @Column(nullable = false)
    @Builder.Default
    private Integer pagesRead = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
