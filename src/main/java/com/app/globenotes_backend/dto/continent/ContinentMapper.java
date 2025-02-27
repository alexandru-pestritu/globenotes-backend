package com.app.globenotes_backend.dto.continent;

import com.app.globenotes_backend.entity.Continent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContinentMapper {
    ContinentDTO toDTO(Continent continent);

    Continent toEntity(ContinentDTO continentDTO);

    @Mapping(target = "countries", ignore = true)
    ContinentDetailsDTO toDetailsDTO(Continent continent);
}
