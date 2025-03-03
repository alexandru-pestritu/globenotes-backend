package com.app.globenotes_backend.service.aws;

import com.app.globenotes_backend.dto.request.PresignedRequest;
import com.app.globenotes_backend.dto.response.PresignedResponse;

import java.util.List;

public interface AwsS3Service {
    PresignedResponse generatePresignedUrlForUpload(Long userId, String fileName, String contentType);
    String generatePresignedUrlForGet(Long userId, String key);
    List<PresignedResponse> generatePresignedUrlsForUpload(Long userId, List<PresignedRequest> requests);
    List<PresignedResponse> generatePresignedUrlsForGet(Long userId, List<String> keys);
}
