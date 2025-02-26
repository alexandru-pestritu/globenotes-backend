package com.app.globenotes_backend.dto.user;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private boolean isVerified;
    private Instant createdAt;
    private Instant updatedAt;
}

