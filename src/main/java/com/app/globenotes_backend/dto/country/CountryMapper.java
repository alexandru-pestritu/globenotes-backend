package com.app.globenotes_backend.dto.country;

import com.app.globenotes_backend.dto.continent.ContinentMapper;
import com.app.globenotes_backend.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ContinentMapper.class})
public interface CountryMapper {
    @Mapping(source = "continent.id", target = "continentId")
    CountryDTO toDTO(Country country);

    @Mapping(source = "continentId", target = "continent.id")
    Country toEntity(CountryDTO countryDTO);

    @Mapping(source = "continent", target = "continent")
    CountryDetailsDTO toDetailsDTO(Country country);

    @Mapping(source = "continent", target = "continent")
    Country toEntityFromDetails(CountryDetailsDTO countryDetailsDTO);
}
