package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.country.CountryDTO;
import com.app.globenotes_backend.dto.request.PresignedRequest;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.response.PresignedResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.aws.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Slf4j
@Validated
public class S3Controller {
    private final AwsS3Service awsS3Service;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/presigned")
    public ResponseEntity<HttpResponse> getPresignedUrl(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PresignedRequest request) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        PresignedResponse presignedResponse = awsS3Service.generatePresignedUrlForUpload(userPrincipal.getUserId(), request.getFileName(), request.getContentType());

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Presigned URL generated successfully")
                .data(Map.of("presigned", presignedResponse))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/presigned")
    public ResponseEntity<HttpResponse> getPresignedUrlForDownload(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String key
    ) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        String presignedGetUrl = awsS3Service.generatePresignedUrlForGet(userPrincipal.getUserId(), key);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Presigned GET URL generated successfully")
                .data(Map.of("url", presignedGetUrl))
                .build();

        return ResponseEntity.ok(response);
    }
}
