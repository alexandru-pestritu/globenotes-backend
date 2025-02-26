package com.app.globenotes_backend.dto.location;

import com.app.globenotes_backend.dto.country.CountryDetailsDTO;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDetailsDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private String formattedAddress;
    private String city;
    private String state;
    private CountryDetailsDTO country;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
}
