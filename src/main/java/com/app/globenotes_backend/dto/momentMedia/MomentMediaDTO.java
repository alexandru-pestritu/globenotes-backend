package com.app.globenotes_backend.dto.momentMedia;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomentMediaDTO {
    private Long id;
    private Long momentId;
    private String mediaUrl;
    private String mediaType;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
}
