package com.app.globenotes_backend.dto.location;

import com.app.globenotes_backend.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(source = "country.id", target = "countryId")
    LocationDTO toDTO(Location location);

    @Mapping(source = "countryId", target = "country.id")
    Location toEntity(LocationDTO locationDTO);
}
