package com.app.globenotes_backend.service.social;

import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserSocialAccount;
import com.app.globenotes_backend.repository.UserSocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {

    private final UserSocialAccountRepository socialRepo;

    @Override
    public Optional<UserSocialAccount> findByProviderAndProviderId(String provider, String providerId) {
        return socialRepo.findByProviderAndProviderId(provider, providerId);
    }

    @Override
    public UserSocialAccount linkSocialAccount(User user, String provider, String providerId) {
        UserSocialAccount social = new UserSocialAccount();
        social.setUser(user);
        social.setProvider(provider);
        social.setProviderId(providerId);
        return socialRepo.save(social);
    }
}