package com.app.globenotes_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLoginRequest {
    @NotBlank(message = "ID token is required")
    private String idToken;
}