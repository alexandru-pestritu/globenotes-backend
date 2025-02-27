package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.userProfile.UserProfileDetailsDTO;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.user.UserService;
import com.app.globenotes_backend.service.userProfile.UserProfileService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final UserProfileService userProfileService;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        UserProfileDetailsDTO userProfile = userProfileService.getProfileDetailsByUserId(userPrincipal.getUserId());

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("User profile retrieved successfully")
                .data(Map.of("profile", userProfile))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PutMapping("/profile")
    public ResponseEntity<HttpResponse> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UserProfileDetailsDTO userProfileDetailsDTO) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        UserProfileDetailsDTO userProfile = userProfileService.updateProfileDetails(userPrincipal.getUserId(), userProfileDetailsDTO);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("User profile updated successfully")
                .data(Map.of("profile", userProfile))
                .build();

        return ResponseEntity.ok(response);
    }

}
