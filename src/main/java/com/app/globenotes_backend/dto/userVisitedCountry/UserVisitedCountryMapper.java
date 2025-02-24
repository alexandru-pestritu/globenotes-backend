package com.app.globenotes_backend.dto.userVisitedCountry;

import com.app.globenotes_backend.entity.UserVisitedCountry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserVisitedCountryMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "country.id", target = "countryId")
    UserVisitedCountryDTO toDTO(UserVisitedCountry userVisitedCountry);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "countryId", target = "country.id")
    UserVisitedCountry toEntity(UserVisitedCountryDTO userVisitedCountryDTO);
}

