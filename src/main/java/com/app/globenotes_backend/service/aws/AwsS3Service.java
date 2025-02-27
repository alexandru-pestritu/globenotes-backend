package com.app.globenotes_backend.service.aws;

import com.app.globenotes_backend.dto.response.PresignedResponse;

public interface AwsS3Service {
    PresignedResponse generatePresignedUrlForUpload(Long userId, String fileName, String contentType);
    String generatePresignedUrlForGet(Long userId, String key);
}
