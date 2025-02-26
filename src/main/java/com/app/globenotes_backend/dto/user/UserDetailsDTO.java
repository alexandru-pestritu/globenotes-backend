package com.app.globenotes_backend.dto.user;

import com.app.globenotes_backend.dto.userProfile.UserProfileDetailsDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDTO {
    private Long id;
    private String email;
    private boolean isVerified;
    private UserProfileDetailsDTO profile;
    private Instant createdAt;
    private Instant updatedAt;
}
