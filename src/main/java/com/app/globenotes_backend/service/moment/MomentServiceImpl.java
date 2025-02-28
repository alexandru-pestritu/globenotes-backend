package com.app.globenotes_backend.service.moment;

import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;
import com.app.globenotes_backend.dto.moment.MomentMapper;
import com.app.globenotes_backend.dto.momentMedia.MomentMediaDTO;
import com.app.globenotes_backend.entity.Journal;
import com.app.globenotes_backend.entity.Location;
import com.app.globenotes_backend.entity.Moment;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.repository.JournalRepository;
import com.app.globenotes_backend.repository.MomentMediaRepository;
import com.app.globenotes_backend.repository.MomentRepository;
import com.app.globenotes_backend.service.location.LocationService;
import com.app.globenotes_backend.service.momentMedia.MomentMediaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public MomentDetailsDTO updateMoment(Long userId, MomentDetailsDTO momentDetailsDTO) {
        Moment existingMoment = momentRepository
                .findByIdAndJournal_User_IdAndIsDeletedFalse(momentDetailsDTO.getId(), userId)
                .orElseThrow(() -> new ApiException("Moment not found or doesn't belong to this user"));

        existingMoment.setName(momentDetailsDTO.getName());
        existingMoment.setDescription(momentDetailsDTO.getDescription());
        existingMoment.setDateTime(momentDetailsDTO.getDateTime());

        if (momentDetailsDTO.getLocation() != null) {
            Location location = locationService.createLocation(momentDetailsDTO.getLocation());
            existingMoment.setLocation(location);
        } else {
            existingMoment.setLocation(null);
        }

        existingMoment = momentRepository.save(existingMoment);

        if (momentDetailsDTO.getMomentMediaList() != null) {
            for (MomentMediaDTO mediaDTO : momentDetailsDTO.getMomentMediaList()) {
                mediaDTO.setMomentId(existingMoment.getId());
                momentMediaService.createMomentMedia(mediaDTO);
            }
        }

        existingMoment = momentRepository.findById(existingMoment.getId())
                .orElseThrow(() -> new ApiException("Moment could not be reloaded after update"));

        return momentMapper.toDetailsDTO(existingMoment);
    }

    @Override
    public void deleteMoment(Long userId, Long momentId) {
        Moment moment = momentRepository
                .findByIdAndJournal_User_IdAndIsDeletedFalse(momentId, userId)
                .orElseThrow(() -> new ApiException("Moment not found or doesn't belong to this user"));

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
