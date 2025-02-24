package com.app.globenotes_backend.dto.userProfile;

import com.app.globenotes_backend.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "location.id", target = "locationId")
    UserProfileDTO toDTO(UserProfile userProfile);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "locationId", target = "location.id")
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}
