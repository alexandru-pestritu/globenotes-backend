package com.app.globenotes_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresignedResponse {
    private String key;
    private String presignedUrl;
}
