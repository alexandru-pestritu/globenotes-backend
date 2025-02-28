package com.app.globenotes_backend.service.sync;

import com.app.globenotes_backend.dto.journal.JournalMapper;
import com.app.globenotes_backend.dto.moment.MomentMapper;
import com.app.globenotes_backend.dto.sync.SyncDTO;
import com.app.globenotes_backend.dto.user.UserMapper;
import com.app.globenotes_backend.dto.userProfile.UserProfileMapper;
import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryMapper;
import com.app.globenotes_backend.entity.*;
import com.app.globenotes_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserVisitedCountryRepository userVisitedCountryRepository;
    private final JournalRepository journalRepository;
    private final MomentRepository momentRepository;

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserVisitedCountryMapper userVisitedCountryMapper;
    private final JournalMapper journalMapper;
    private final MomentMapper momentMapper;

    @Override
    public SyncDTO getSyncData(Long userId, Instant lastSync) {
        SyncDTO syncDTO = new SyncDTO();

        Optional<User> user = userRepository.findByIdAndUpdatedAtAfter(userId, lastSync);
        user.ifPresent(value -> syncDTO.setUser(userMapper.toDTO(value)));

        Optional<UserProfile> userProfile = userProfileRepository.findByUserIdAndUpdatedAtAfter(userId, lastSync);
        userProfile.ifPresent(value -> syncDTO.setUserProfile(userProfileMapper.toDetailsDTO(value)));

        List<UserVisitedCountry> userVisitedCountries = userVisitedCountryRepository.findByUserIdAndVisitedAtAfter(userId, lastSync);
        syncDTO.setUserVisitedCountries(userVisitedCountries.stream()
                .map(userVisitedCountryMapper::toDetailsDTO)
                .toList());

        List<Journal> journals = journalRepository.findAllByUserIdAndUpdatedAtAfter(userId, lastSync);
        syncDTO.setJournals(journals.stream()
                .map(journalMapper::toDTO)
                .toList());

        List<Moment> moments = momentRepository.findAllByJournal_User_IdAndUpdatedAtAfter(userId, lastSync);
        syncDTO.setMoments(moments.stream()
                .map(momentMapper::toDetailsDTO)
                .toList());

        return syncDTO;
    }
}
