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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@Slf4j
@Validated
public class S3Controller {
    private final AwsS3Service awsS3Service;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/presigned/put")
    public ResponseEntity<HttpResponse> getPresignedUrls(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody List<PresignedRequest> requests) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<PresignedResponse> presignedResponses = requests.stream()
                .map(req -> awsS3Service.generatePresignedUrlForUpload(userPrincipal.getUserId(), req.getFileName(), req.getContentType()))
                .toList();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Presigned URLs generated successfully")
                .data(Map.of("presigned", presignedResponses))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/presigned/get")
    public ResponseEntity<HttpResponse> getPresignedUrlsForDownload(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody List<String> keys
    ) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<PresignedResponse> presignedResponses = keys.stream()
                .map(key -> new PresignedResponse(key, awsS3Service.generatePresignedUrlForGet(userPrincipal.getUserId(), key)))
                .toList();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Presigned GET URLs generated successfully")
                .data(Map.of("urls", presignedResponses))
                .build();

        return ResponseEntity.ok(response);
    }
}
