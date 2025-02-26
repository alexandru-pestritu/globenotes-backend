package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.continent.ContinentService;
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
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/continent")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ContinentController {
    private final ContinentService continentService;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/all")
    public ResponseEntity<HttpResponse> getAllContinents(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<ContinentDTO> continentDTOs = continentService.getAllContinents();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("All continents retrieved successfully")
                .data(Map.of("continents", continentDTOs))
                .build();

        return ResponseEntity.ok(response);
    }

}
