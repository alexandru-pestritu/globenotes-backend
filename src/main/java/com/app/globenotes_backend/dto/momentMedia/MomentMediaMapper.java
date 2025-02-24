package com.app.globenotes_backend.dto.momentMedia;

import com.app.globenotes_backend.entity.MomentMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MomentMediaMapper {
    @Mapping(source = "moment.id", target = "momentId")
    MomentMediaDTO toDTO(MomentMedia momentMedia);

    @Mapping(source = "momentId", target = "moment.id")
    MomentMedia toEntity(MomentMediaDTO momentMediaDTO);
}

