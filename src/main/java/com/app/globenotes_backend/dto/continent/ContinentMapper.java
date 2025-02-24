package com.app.globenotes_backend.dto.continent;

import com.app.globenotes_backend.entity.Continent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContinentMapper {
    ContinentDTO toDTO(Continent continent);
    Continent toEntity(ContinentDTO continentDTO);
}
