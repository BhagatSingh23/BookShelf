package com.bookshelf.dto.response;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SavedEntryResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String entryType;
    private String content;
    private String pageRef;
    private LocalDateTime createdAt;
}
