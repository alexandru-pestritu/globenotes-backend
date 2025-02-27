package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import com.app.globenotes_backend.dto.continent.ContinentDetailsDTO;
import com.app.globenotes_backend.dto.country.CountryDTO;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.continent.ContinentService;
import com.app.globenotes_backend.service.country.CountryService;
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
@RequestMapping("/api/location")
@RequiredArgsConstructor
@Slf4j
@Validated
public class LocationController {
    private final ContinentService continentService;
    private final CountryService countryService;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/continents")
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

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/countries")
    public ResponseEntity<HttpResponse> getAllCountries(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<CountryDTO> countryDTOs = countryService.getAllCountries();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("All countries retrieved successfully")
                .data(Map.of("countries", countryDTOs))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/continents/countries")
    public ResponseEntity<HttpResponse> getAllContinentsWithCountries(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<ContinentDetailsDTO> continentDetailsDTOs = continentService.getAllContinentsWithCountries();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("All continents with countries retrieved successfully")
                .data(Map.of("continents", continentDetailsDTOs))
                .build();

        return ResponseEntity.ok(response);
    }
}
