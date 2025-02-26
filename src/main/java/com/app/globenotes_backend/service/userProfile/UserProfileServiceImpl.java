package com.app.globenotes_backend.service.userProfile;

import com.app.globenotes_backend.dto.userProfile.UserProfileDTO;
import com.app.globenotes_backend.dto.userProfile.UserProfileMapper;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserProfile;
import com.app.globenotes_backend.repository.UserProfileRepository;
import com.app.globenotes_backend.repository.UserRepository;
import com.app.globenotes_backend.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDTO createProfile(Long userId, String name) {
        Optional<UserProfile> existing = userProfileRepository.findByUserId(userId);

        if (existing.isPresent()) {
            return userProfileMapper.toDTO(existing.get());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setName(name);
        return userProfileMapper.toDTO(userProfileRepository.save(profile));
    }

    @Override
    public UserProfileDTO getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));
        return userProfileMapper.toDTO(profile);
    }

    @Override
    public void updateProfilePhoto(Long userProfileId, String newPhotoUrl) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));

        profile.setProfilePhotoUrl(newPhotoUrl);
        userProfileRepository.save(profile);
    }
}