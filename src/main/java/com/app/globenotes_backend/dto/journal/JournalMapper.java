package com.app.globenotes_backend.dto.journal;

import com.app.globenotes_backend.entity.Journal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JournalMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tripLocation.id", target = "tripLocationId")
    JournalDTO toDTO(Journal journal);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "tripLocationId", target = "tripLocation.id")
    Journal toEntity(JournalDTO journalDTO);
}

