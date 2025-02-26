package com.app.globenotes_backend.dto.userVisitedCountry;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVisitedCountryDTO {
    private Long id;
    private Long userId;
    private Long countryId;
    private Instant visitedAt;
    private Boolean isDeleted;
}

