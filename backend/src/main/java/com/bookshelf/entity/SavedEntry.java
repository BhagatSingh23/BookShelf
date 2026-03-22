package com.bookshelf.entity;

import jakarta.persistence.*;
// lombok dependency auto creates constructors and getters and setters
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_entries")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SavedEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // QUOTE | NOTE | INSIGHT | FACT | REFLECTION
    @Column(nullable = false, length = 50)
    private String entryType = "QUOTE";

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 20)
    private String pageRef; // optional e.g. "p.142"

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
