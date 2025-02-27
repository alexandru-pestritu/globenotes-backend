package com.app.globenotes_backend.dto.journal;

import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;
import com.app.globenotes_backend.entity.Location;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalDetailsDTO {
    private Long id;
    private Long userId;
    private String name;
    private String shortSummary;
    private LocationDetailsDTO tripLocation;
    private String coverPhotoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean remindersEnabled;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
    List<MomentDetailsDTO> moments;
}
