package com.app.globenotes_backend.dto.moment;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomentDTO {
    private Long id;
    private Long journalId;
    private Long locationId;
    private String name;
    private String description;
    private Long categoryId;
    private Instant dateTime;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
}

