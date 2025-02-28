package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.sync.SyncDTO;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.sync.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SyncController {
    private final SyncService syncService;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/")
    public ResponseEntity<HttpResponse> sync(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String lastSync
    ) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        Instant lastSyncInstant = Instant.parse(lastSync);

        SyncDTO syncDTO = syncService.getSyncData(userPrincipal.getUserId(), lastSyncInstant);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Sync data retrieved successfully")
                .data(Map.of("sync", syncDTO))
                .build();

        return ResponseEntity.ok(response);
    }
}
