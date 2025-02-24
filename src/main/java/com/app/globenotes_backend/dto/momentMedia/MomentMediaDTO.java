package com.app.globenotes_backend.dto.momentMedia;

import lombok.*;

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
}
