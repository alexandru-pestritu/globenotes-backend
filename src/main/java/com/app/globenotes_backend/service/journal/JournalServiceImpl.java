package com.app.globenotes_backend.service.journal;

import com.app.globenotes_backend.dto.journal.JournalDTO;
import com.app.globenotes_backend.dto.journal.JournalDetailsDTO;
import com.app.globenotes_backend.dto.journal.JournalMapper;
import com.app.globenotes_backend.entity.Journal;
import com.app.globenotes_backend.entity.Location;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.repository.JournalRepository;
import com.app.globenotes_backend.service.location.LocationService;
import com.app.globenotes_backend.service.moment.MomentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService{
    private final JournalRepository journalRepository;
    private final JournalMapper journalMapper;
    private final LocationService locationService;
    private final MomentService momentService;

    @Override
    public JournalDTO createJournal(JournalDTO journalDTO) {
       Journal journal = journalMapper.toEntity(journalDTO);

       Location location = locationService.createLocation(journalDTO.getTripLocation());
       journal.setTripLocation(location);
       return journalMapper.toDTO(journalRepository.save(journal));
    }

    @Override
    public JournalDetailsDTO getJournalDetails(Long userId, Long journalId) {
        Optional<Journal> journal = journalRepository.findByUserIdAndId(userId, journalId);

        return journal.map(journalMapper::toDetailsDTO).orElseThrow(() -> new ApiException("Journal not found"));
    }

    @Override
    public JournalDTO updateJournal(JournalDTO journalDTO) {
        Optional<Journal> journal = journalRepository.findByUserIdAndId(journalDTO.getUserId(), journalDTO.getId());

        if(journal.isEmpty()) {
            throw new ApiException("Journal not found");
        }
        else {
            Journal updatedJournal = journalMapper.toEntity(journalDTO);
            updatedJournal.setTripLocation(locationService.createLocation(journalDTO.getTripLocation()));
            return journalMapper.toDTO(journalRepository.save(updatedJournal));
        }
    }

    @Override
    public void deleteJournal(Long userId, Long journalId) {
        Optional<Journal> journal = journalRepository.findByUserIdAndId(userId, journalId);

        if(journal.isEmpty()) {
            throw new ApiException("Journal not found");
        }
        else {
            journal.get().setIsDeleted(true);
            journal.get().getMoments().forEach(moment -> momentService.deleteMoment(userId, moment.getId()));
            journalRepository.save(journal.get());
        }

    }

    @Override
    public List<JournalDTO> getJournals(Long userId) {
        return journalRepository.findAllByUserId(userId).stream()
                .map(journalMapper::toDTO)
                .toList();
    }
}
