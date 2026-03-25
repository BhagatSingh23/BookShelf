package com.bookshelf.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyRequest {
    @Email @NotBlank public String email;
    @NotBlank public String code;
}
