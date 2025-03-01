package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.category.CategoryDTO;
import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.category.CategoryService;
import com.app.globenotes_backend.service.moment.MomentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moment")
@RequiredArgsConstructor
public class MomentController {

    private final MomentService momentService;
    private final CategoryService categoryService;

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/categories")
    public ResponseEntity<HttpResponse> getAllCategories(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Moment categories retrieved successfully")
                .data(Map.of("categories", categoryDTOs))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/")
    public ResponseEntity<HttpResponse> createMoment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody MomentDetailsDTO momentDetailsDTO) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        MomentDetailsDTO createdMoment = momentService.addMoment(userPrincipal.getUserId(), momentDetailsDTO);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Moment created successfully")
                .data(Map.of("moment", createdMoment))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/")
    public ResponseEntity<HttpResponse> updateMoment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody MomentDetailsDTO momentDetailsDTO) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        MomentDetailsDTO updatedMoment = momentService.updateMoment(userPrincipal.getUserId(), momentDetailsDTO);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Moment updated successfully")
                .data(Map.of("moment", updatedMoment))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/{momentId}")
    public ResponseEntity<HttpResponse> deleteMoment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable Long momentId) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        momentService.deleteMoment(userPrincipal.getUserId(), momentId);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Moment deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/{momentId}")
    public ResponseEntity<HttpResponse> getMoment(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable Long momentId) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        MomentDetailsDTO momentDetailsDTO = momentService.getMoment(userPrincipal.getUserId(), momentId);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Moment retrieved successfully")
                .data(Map.of("moment", momentDetailsDTO))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/journal/{journalId}")
    public ResponseEntity<HttpResponse> getMomentsByJournal(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @PathVariable Long journalId) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<MomentDetailsDTO> moments =
                momentService.getAllMomentsByJournal(userPrincipal.getUserId(), journalId);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Moments retrieved successfully")
                .data(Map.of("moments", moments))
                .build();

        return ResponseEntity.ok(response);
    }
}
