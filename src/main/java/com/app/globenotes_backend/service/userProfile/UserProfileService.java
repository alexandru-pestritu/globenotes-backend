package com.app.globenotes_backend.service.userProfile;

import com.app.globenotes_backend.model.User;
import com.app.globenotes_backend.model.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfile createProfile(User user);

    Optional<UserProfile> getProfileByUserId(Long userId);

    UserProfile updateProfilePhoto(Long userProfileId, String newPhotoUrl);

    UserProfile updateCoverPhoto(Long userProfileId, String newCoverUrl);

    UserProfile updateBio(Long userProfileId, String newBio);

    UserProfile updateLocation(Long userProfileId, Long locationId);

}