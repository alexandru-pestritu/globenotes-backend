package com.app.globenotes_backend.dto.country;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDetailsDTO {
    private Long id;
    private String name;
    private String isoCode;
    private ContinentDTO continent;
}