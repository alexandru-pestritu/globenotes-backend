package com.app.globenotes_backend.service.aws;

import com.app.globenotes_backend.dto.request.PresignedRequest;
import com.app.globenotes_backend.dto.response.PresignedResponse;
import com.app.globenotes_backend.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

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

    @Override
    public List<PresignedResponse> generatePresignedUrlsForUpload(Long userId, List<PresignedRequest> requests) {
        return requests.stream()
                .map(req -> generatePresignedUrlForUpload(userId, req.getFileName(), req.getContentType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PresignedResponse> generatePresignedUrlsForGet(Long userId, List<String> keys) {
        return keys.stream()
                .map(key -> new PresignedResponse(key, generatePresignedUrlForGet(userId, key)))
                .collect(Collectors.toList());
    }

    @Override
    public String uploadFileFromUrl(Long userId, String externalUrl, String fileName) {
        String key = "uploads/" + userId + "/" + System.currentTimeMillis() + "_" + fileName;

        try (InputStream inputStream = new URL(externalUrl).openStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] tmp = new byte[8_192];
            int bytesRead;
            while ((bytesRead = inputStream.read(tmp)) != -1) {
                buffer.write(tmp, 0, bytesRead);
            }

            byte[] fileBytes = buffer.toByteArray();

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(
                    putRequest,
                    RequestBody.fromBytes(fileBytes)
            );

            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3 from external URL", e);
        }
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
