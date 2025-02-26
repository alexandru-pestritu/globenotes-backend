package com.app.globenotes_backend.dto.location;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private String formattedAddress;
    private String city;
    private String state;
    private Long countryId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
}

