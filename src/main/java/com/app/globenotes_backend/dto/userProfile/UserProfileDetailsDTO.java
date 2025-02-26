package com.app.globenotes_backend.dto.userProfile;

import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDetailsDTO {
    private Long id;
    private String name;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String bio;
    private LocationDetailsDTO location;
    private Instant createdAt;
    private Instant updatedAt;
}