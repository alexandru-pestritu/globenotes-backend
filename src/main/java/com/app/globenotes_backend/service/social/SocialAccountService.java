package com.app.globenotes_backend.service.social;

import com.app.globenotes_backend.model.User;
import com.app.globenotes_backend.model.UserSocialAccount;

import java.util.Optional;

public interface SocialAccountService {

    Optional<UserSocialAccount> findByProviderAndProviderId(String provider, String providerId);

    UserSocialAccount linkSocialAccount(User user, String provider, String providerId);
}
