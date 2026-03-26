package com.bookshelf.dto.response;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String token;
    private String name;
    private String email;
}
