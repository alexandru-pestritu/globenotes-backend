package com.app.globenotes_backend.service.moment;

import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;
import com.app.globenotes_backend.dto.moment.MomentMapper;
import com.app.globenotes_backend.dto.momentMedia.MomentMediaDTO;
import com.app.globenotes_backend.entity.Journal;
import com.app.globenotes_backend.entity.Location;
import com.app.globenotes_backend.entity.Moment;
import com.app.globenotes_backend.entity.MomentMedia;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.repository.JournalRepository;
import com.app.globenotes_backend.repository.MomentMediaRepository;
import com.app.globenotes_backend.repository.MomentRepository;
import com.app.globenotes_backend.service.location.LocationService;
import com.app.globenotes_backend.service.momentMedia.MomentMediaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MomentServiceImpl implements MomentService{
    private final MomentRepository momentRepository;
    private final MomentMapper momentMapper;
    private final MomentMediaRepository momentMediaRepository;
    private final MomentMediaService momentMediaService;
    private final LocationService locationService;
    private final JournalRepository journalRepository;

    @Override
    public MomentDetailsDTO addMoment(Long userId, MomentDetailsDTO momentDetailsDTO) {
        Journal journal = journalRepository.findByUserIdAndId(userId, momentDetailsDTO.getJournalId())
                .orElseThrow(() -> new ApiException("Journal not found for the current user"));

        Moment moment = momentMapper.toEntityFromDetails(momentDetailsDTO);
        moment.setJournal(journal);

        if (momentDetailsDTO.getLocation() != null) {
            Location location = locationService.createLocation(momentDetailsDTO.getLocation());
            moment.setLocation(location);
        }

        moment = momentRepository.save(moment);

        if (momentDetailsDTO.getMomentMediaList() != null) {
            for (MomentMediaDTO mediaDTO : momentDetailsDTO.getMomentMediaList()) {
                mediaDTO.setMomentId(moment.getId());
                momentMediaService.createMomentMedia(mediaDTO);
            }
        }

        moment = momentRepository.findById(moment.getId())
                .orElseThrow(() -> new ApiException("Moment could not be reloaded after save"));
        moment.setMomentMediaList(momentMediaRepository.findAllByMoment_Id(moment.getId()));

        return momentMapper.toDetailsDTO(moment);
    }

    @Override
    @Transactional
    public MomentDetailsDTO updateMoment(Long userId, MomentDetailsDTO dto) {

        Moment moment = momentRepository
                .findByIdAndJournal_User_IdAndIsDeletedFalse(dto.getId(), userId)
                .orElseThrow(() -> new ApiException("Moment not found or doesn't belong to this user"));

        if (moment.getUpdatedAt().isAfter(dto.getUpdatedAt()))
            return momentMapper.toDetailsDTO(moment);

        moment.setName(dto.getName());
        moment.setDescription(dto.getDescription());
        moment.setDateTime(dto.getDateTime());

        if (dto.getLocation() != null) {
            Location loc = locationService.createLocation(dto.getLocation());
            moment.setLocation(loc);
        } else {
            moment.setLocation(null);
        }
        moment.setUpdatedAt(Instant.now());
        moment = momentRepository.save(moment);

        List<MomentMedia> currentMedia = momentMediaRepository
                .findAllByMoment_IdAndIsDeletedFalse(moment.getId());

        Map<Long, MomentMedia>    byId  = currentMedia.stream()
                .collect(Collectors.toMap(MomentMedia::getId, m -> m));
        Map<String, MomentMedia>  byUrl = currentMedia.stream()
                .collect(Collectors.toMap(MomentMedia::getMediaUrl, m -> m));

        Set<Long> keepIds = new HashSet<>();

        if (dto.getMomentMediaList() != null) {
            for (MomentMediaDTO mediaDTO : dto.getMomentMediaList()) {
                MomentMedia mm = null;
                if (mediaDTO.getId() != null) {
                    mm = byId.get(mediaDTO.getId());
                }
                if (mm == null) {
                    mm = byUrl.get(mediaDTO.getMediaUrl());
                }

                if (mm == null) {
                    mediaDTO.setMomentId(moment.getId());
                    MomentMedia created = momentMediaService.createMomentMedia(mediaDTO);
                    keepIds.add(created.getId());
                } else {
                    mm.setMediaType(mediaDTO.getMediaType());
                    mm.setUpdatedAt(mediaDTO.getUpdatedAt());
                    momentMediaRepository.save(mm);
                    keepIds.add(mm.getId());
                }
            }
        }

        momentMediaRepository.deleteByMoment_IdAndIdNotIn(moment.getId(), keepIds);

        moment = momentRepository.findById(moment.getId())
                .orElseThrow(() -> new ApiException("Moment could not be reloaded after update"));

        return momentMapper.toDetailsDTO(moment);
    }


    @Override
    public void deleteMoment(Long userId, Long momentId) {
        Moment moment = momentRepository
                .findByIdAndJournal_User_IdAndIsDeletedFalse(momentId, userId)
                .orElseThrow(() -> new ApiException("Moment not found or doesn't belong to this user"));

        momentMediaRepository.findAllByMoment_Id(momentId)
                .forEach(media -> media.setIsDeleted(true));

        moment.setIsDeleted(true);
        momentRepository.save(moment);
    }

    @Override
    public MomentDetailsDTO getMoment(Long userId, Long momentId) {
        Moment moment = momentRepository
                .findByIdAndJournal_User_IdAndIsDeletedFalse(momentId, userId)
                .orElseThrow(() -> new ApiException("Moment not found or doesn't belong to this user"));

        return momentMapper.toDetailsDTO(moment);
    }

    @Override
    public List<MomentDetailsDTO> getAllMomentsByJournal(Long userId, Long journalId) {
        List<Moment> moments = momentRepository
                .findAllByJournal_IdAndJournal_User_IdAndIsDeletedFalse(journalId, userId);

        return moments.stream()
                .map(momentMapper::toDetailsDTO)
                .toList();
    }
}
