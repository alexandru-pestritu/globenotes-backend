package com.app.globenotes_backend.dto.userProfile;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private Long userId;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String bio;
    private Long locationId;
}

