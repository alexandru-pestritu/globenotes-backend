package com.app.globenotes_backend.dto.moment;

import com.app.globenotes_backend.dto.category.CategoryDTO;
import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MomentDetailsDTO {
    private Long id;
    private Long journalId;
    private LocationDetailsDTO location;
    private String name;
    private String description;
    private CategoryDTO category;
    private Instant dateTime;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isDeleted;
}
