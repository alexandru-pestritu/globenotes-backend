package com.app.globenotes_backend.dto.request;

import lombok.Data;

@Data
public class SocialLoginRequest {
    private String provider;
    private String idToken;
}