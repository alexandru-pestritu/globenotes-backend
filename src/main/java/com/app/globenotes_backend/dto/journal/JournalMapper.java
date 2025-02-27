package com.app.globenotes_backend.dto.journal;

import com.app.globenotes_backend.dto.location.LocationMapper;
import com.app.globenotes_backend.dto.moment.MomentMapper;
import com.app.globenotes_backend.entity.Journal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, MomentMapper.class})
public interface JournalMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tripLocation", target = "tripLocation")
    JournalDTO toDTO(Journal journal);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tripLocation", target = "tripLocation")
    Journal toEntity(JournalDTO journalDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tripLocation", target = "tripLocation")
    @Mapping(target = "moments", ignore = true)
    JournalDetailsDTO toDetailsDTO(Journal journal);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tripLocation", target = "tripLocation")
    @Mapping(target = "moments", ignore = true)
    Journal toEntityFromDetails(JournalDetailsDTO journalDetailsDTO);
}

