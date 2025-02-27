package com.app.globenotes_backend.dto.moment;

import com.app.globenotes_backend.dto.category.CategoryMapper;
import com.app.globenotes_backend.dto.location.LocationMapper;
import com.app.globenotes_backend.dto.momentMedia.MomentMediaMapper;
import com.app.globenotes_backend.entity.Moment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MomentMediaMapper.class, LocationMapper.class, CategoryMapper.class})
public interface MomentMapper {
    @Mapping(source = "journal.id", target = "journalId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "category.id", target = "categoryId")
    MomentDTO toDTO(Moment moment);

    @Mapping(source = "journalId", target = "journal.id")
    @Mapping(source = "locationId", target = "location.id")
    @Mapping(source = "categoryId", target = "category.id")
    Moment toEntity(MomentDTO momentDTO);

    @Mapping(source = "journal.id", target = "journalId")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "category", target = "category")
    @Mapping(target = "momentMediaList", ignore = true)
    MomentDetailsDTO toDetailsDTO(Moment moment);

    @Mapping(source = "journalId", target = "journal.id")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "category", target = "category")
    @Mapping(target = "momentMediaList", ignore = true)
    Moment toEntityFromDetails(MomentDetailsDTO momentDetailsDTO);
}

