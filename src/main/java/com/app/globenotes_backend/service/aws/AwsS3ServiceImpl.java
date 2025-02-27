package com.app.globenotes_backend.service.aws;

import com.app.globenotes_backend.dto.response.PresignedResponse;
import com.app.globenotes_backend.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;

    public static final int LINK_EXPIRATION_TIME = 15;

    public PresignedResponse generatePresignedUrlForUpload(Long userId, String fileName, String contentType) {

        String key = "uploads/" + userId.toString() + "/" + System.currentTimeMillis() + "_" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(LINK_EXPIRATION_TIME))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String presignedUrl = presignedRequest.url().toString();

        return new PresignedResponse(key, presignedUrl);
    }

    @Override
    public String generatePresignedUrlForGet(Long userId, String key) {
        Long userIdFromKey = parseUserIdFromKey(key);

        if (!userIdFromKey.equals(userId)) {
            throw new ApiException("You do not have permission to access this file");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(LINK_EXPIRATION_TIME))
                        .build()
        );

        return presignedGetObjectRequest.url().toString();
    }

    private Long parseUserIdFromKey(String key) {
        String afterUploads = key.substring("uploads/".length());

        int slashIndex = afterUploads.indexOf("/");
        if (slashIndex == -1) {
            throw new IllegalArgumentException("Invalid key format, missing userId folder");
        }
        String userIdStr = afterUploads.substring(0, slashIndex);

        return Long.valueOf(userIdStr);
    }

}
