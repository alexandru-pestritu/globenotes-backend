package com.app.globenotes_backend.dto.journal;

import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalDTO {
    private Long id;
    private Long userId;
    private String name;
    private String shortSummary;
    private LocationDetailsDTO tripLocation;
    private String coverPhotoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean remindersEnabled;
    private Instant updatedAt;
    private boolean isDeleted;
}

