package com.app.globenotes_backend.dto.journal;

import lombok.*;

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
    private Long tripLocationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean remindersEnabled;
}

