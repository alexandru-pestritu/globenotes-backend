package com.app.globenotes_backend.dto.continent;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContinentDTO {
    private Long id;
    private String name;
    private String code;
}

