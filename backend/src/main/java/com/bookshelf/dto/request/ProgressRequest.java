package com.bookshelf.dto.request;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProgressRequest {
    public Integer pagesRead;
    public Boolean isCompleted;
}
