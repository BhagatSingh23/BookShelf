package com.bookshelf.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SavedEntryRequest {
    // QUOTE | NOTE | INSIGHT | FACT | REFLECTION
    @NotBlank public String entryType;
    @NotBlank public String content;
    public String pageRef;
}
