package com.app.globenotes_backend.dto.user;

import com.app.globenotes_backend.dto.userProfile.UserProfileMapper;
import com.app.globenotes_backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserProfileMapper.class})
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);

    @Mapping(source = "userProfile", target = "profile")
    UserDetailsDTO toUserDetailsDTO(User user);
}
