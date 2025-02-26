package com.app.globenotes_backend.service.userProfile;

import com.app.globenotes_backend.dto.userProfile.UserProfileDTO;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfileDTO createProfile(Long userId, String name);

    UserProfileDTO getProfileByUserId(Long userId);

    void updateProfilePhoto(Long userProfileId, String newPhotoUrl);

}