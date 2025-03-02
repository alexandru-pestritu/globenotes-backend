package com.app.globenotes_backend.dto.moment;

import com.app.globenotes_backend.dto.category.CategoryDTO;
import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import com.app.globenotes_backend.dto.momentMedia.MomentMediaDTO;
import lombok.*;

import java.time.Instant;
import java.util.List;

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
    private Instant updatedAt;
    private boolean isDeleted;
    List<MomentMediaDTO> momentMediaList;
}
