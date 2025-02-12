package com.app.globenotes_backend.dto.request;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String email;
    private String code;
}