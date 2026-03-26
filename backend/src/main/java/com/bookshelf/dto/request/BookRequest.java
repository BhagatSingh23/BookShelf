package com.bookshelf.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class BookRequest {
    @NotBlank public String title;
    public String author;
    public String genre;
    public Integer totalPages;
    public String coverUrl;
    public String olBookId;
    public String olUrl;
}
