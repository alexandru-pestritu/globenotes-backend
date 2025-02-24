package com.app.globenotes_backend.dto.country;

import com.app.globenotes_backend.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    @Mapping(source = "continent.id", target = "continentId")
    CountryDTO toDTO(Country country);

    @Mapping(source = "continentId", target = "continent.id")
    Country toEntity(CountryDTO countryDTO);
}
