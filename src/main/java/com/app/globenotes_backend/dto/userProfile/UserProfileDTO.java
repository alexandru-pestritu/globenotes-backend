package com.app.globenotes_backend.dto.userProfile;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private Long userId;
    private String name;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String bio;
    private Long locationId;
    private Instant createdAt;
    private Instant updatedAt;
}

