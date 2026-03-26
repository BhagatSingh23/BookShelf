package com.bookshelf.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private Integer totalPages;
    private String coverUrl;
    private String olBookId;
    private String olUrl;
    private LocalDateTime addedAt;
    // reading progress (included for convenience)
    private Integer pagesRead;
    private Boolean isCompleted;
}
