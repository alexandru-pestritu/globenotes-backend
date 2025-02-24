package com.app.globenotes_backend.dto.moment;

import com.app.globenotes_backend.entity.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MomentMapper {
    @Mapping(source = "journal.id", target = "journalId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "category.id", target = "categoryId")
    MomentDTO toDTO(Moment moment);

    @Mapping(source = "journalId", target = "journal.id")
    @Mapping(source = "locationId", target = "location.id")
    @Mapping(source = "categoryId", target = "category.id")
    Moment toEntity(MomentDTO momentDTO);
}

