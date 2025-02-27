package com.app.globenotes_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresignedRequest {
    private String fileName;
    private String contentType;
}
