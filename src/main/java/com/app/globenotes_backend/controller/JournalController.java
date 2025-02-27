package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.journal.JournalDTO;
import com.app.globenotes_backend.dto.journal.JournalDetailsDTO;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.security.UserPrincipal;
import com.app.globenotes_backend.service.journal.JournalService;
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
@RequestMapping("/api/journal")
@RequiredArgsConstructor
@Slf4j
@Validated
public class JournalController {
    private final JournalService journalService;

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/")
    public ResponseEntity<HttpResponse> createJournal(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestBody JournalDTO journalDTO) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        journalDTO.setUserId(userPrincipal.getUserId());
        JournalDTO createdJournal = journalService.createJournal(journalDTO);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Journal created successfully")
                .data(Map.of("journal", createdJournal))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/{journalId}")
    public ResponseEntity<HttpResponse> getJournalDetails(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @PathVariable Long journalId) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        JournalDetailsDTO journalDetails = journalService.getJournalDetails(userPrincipal.getUserId(), journalId);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Journal details retrieved successfully")
                .data(Map.of("journal", journalDetails))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/")
    public ResponseEntity<HttpResponse> updateJournal(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestBody JournalDTO journalDTO) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        journalDTO.setUserId(userPrincipal.getUserId());
        JournalDTO updatedJournal = journalService.updateJournal(journalDTO);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Journal updated successfully")
                .data(Map.of("journal", updatedJournal))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/{journalId}")
    public ResponseEntity<HttpResponse> deleteJournal(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @PathVariable Long journalId) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        journalService.deleteJournal(userPrincipal.getUserId(), journalId);

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Journal deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/")
    public ResponseEntity<HttpResponse> getJournals(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new ApiException("User is not authenticated");
        }

        List<JournalDTO> journals = journalService.getJournals(userPrincipal.getUserId());

        HttpResponse response = HttpResponse.builder()
                .timeStamp(Instant.now().toString())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Journals retrieved successfully")
                .data(Map.of("journals", journals))
                .build();

        return ResponseEntity.ok(response);
    }
}
