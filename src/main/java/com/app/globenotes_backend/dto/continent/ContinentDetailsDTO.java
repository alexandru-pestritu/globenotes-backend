package com.app.globenotes_backend.dto.continent;

import com.app.globenotes_backend.dto.country.CountryDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContinentDetailsDTO {
    private Long id;
    private String name;
    private String code;
    private List<CountryDTO> countries;
}
