package com.app.globenotes_backend.dto.country;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDTO {
    private Long id;
    private Long continentId;
    private String name;
    private String isoCode;
}

