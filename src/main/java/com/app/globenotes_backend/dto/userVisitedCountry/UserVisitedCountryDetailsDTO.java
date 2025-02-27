package com.app.globenotes_backend.dto.userVisitedCountry;

import com.app.globenotes_backend.dto.country.CountryDetailsDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVisitedCountryDetailsDTO {
    private Long id;
    private Long userId;
    private CountryDetailsDTO country;
    private Instant visitedAt;
    private Boolean isDeleted;
}
