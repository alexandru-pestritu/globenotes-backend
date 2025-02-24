package com.app.globenotes_backend.service.userProfile;

import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.Location;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserProfile;
import com.app.globenotes_backend.repository.UserProfileRepository;
import com.app.globenotes_backend.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final LocationService locationService;

    @Override
    public UserProfile createProfile(User user) {
        Optional<UserProfile> existing = userProfileRepository.findByUserId(user.getId());
        if (existing.isPresent()) {
            return existing.get();
        }

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        return userProfileRepository.save(profile);
    }

    @Override
    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public UserProfile updateProfilePhoto(Long userProfileId, String newPhotoUrl) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));

        profile.setProfilePhotoUrl(newPhotoUrl);
        return userProfileRepository.save(profile);
    }

    @Override
    public UserProfile updateCoverPhoto(Long userProfileId, String newCoverUrl) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));

        profile.setCoverPhotoUrl(newCoverUrl);
        return userProfileRepository.save(profile);
    }

    @Override
    public UserProfile updateBio(Long userProfileId, String newBio) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));

        profile.setBio(newBio);
        return userProfileRepository.save(profile);
    }

    @Override
    public UserProfile updateLocation(Long userProfileId, Long locationId) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new ApiException("UserProfile not found"));

        if (locationId == null) {
            profile.setLocation(null);
        } else {
            Location location = locationService.getLocationById(locationId)
                    .orElseThrow(() -> new ApiException("Location not found"));
            profile.setLocation(location);
        }
        return userProfileRepository.save(profile);
    }
}