package com.app.globenotes_backend.service.auth;

import com.app.globenotes_backend.dto.authentication.Authentication;
import com.app.globenotes_backend.dto.social.FacebookUserInfo;
import com.app.globenotes_backend.dto.social.GoogleUserInfo;
import com.app.globenotes_backend.dto.user.UserDTO;
import com.app.globenotes_backend.dto.userProfile.UserProfileDTO;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.*;
import com.app.globenotes_backend.repository.UserRepository;
import com.app.globenotes_backend.service.email.EmailService;
import com.app.globenotes_backend.service.otp.OtpService;
import com.app.globenotes_backend.service.refresh.RefreshTokenService;
import com.app.globenotes_backend.service.social.SocialAccountService;
import com.app.globenotes_backend.service.user.UserService;
import com.app.globenotes_backend.service.userProfile.UserProfileService;
import com.app.globenotes_backend.util.JwtUtils;
import com.app.globenotes_backend.util.SocialAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final SocialAccountService socialAccountService;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final SocialAuthUtil socialAuthUtil;

    private final long OTP_EXPIRY_MINUTES = 10;
    private final long REFRESH_EXPIRY_DAYS = 365;

    @Override
    public void register(String name, String email, String password) {
        UserDTO user = userService.createUser(email, password);
        UserProfileDTO userProfile = userProfileService.createProfile(user.getId(), name);

        String code = generateRandom4Digits();
        otpService.createOtp(user.getId(), "VERIFICATION", code, Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("NAME", userProfile.getName());
        placeholders.put("OTP_CODE", code);

//        emailService.sendTemplatedEmail(
//                user.getEmail(),
//                "email-verification",
//                placeholders
//        );
    }

    @Override
    public void resendOtp(String email) {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        if (user.isVerified()) {
            throw new ApiException("Email already verified");
        }

        UserProfileDTO userProfile = userProfileService.getProfileByUserId(user.getId());

        String code = generateRandom4Digits();
        otpService.createOtp(user.getId(), "VERIFICATION", code, Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("NAME", userProfile.getName());
        placeholders.put("OTP_CODE", code);

//        emailService.sendTemplatedEmail(
//                user.getEmail(),
//                "email-verification",
//                placeholders
//            );
        }


    @Override
    public void verifyEmail(String email, String code) {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user.getId(), "VERIFICATION", code);
        userService.verifyUser(user.getId());
        otpService.markUsed(otp.getId());
    }

    @Override
    public Authentication login(String email, String password) {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));

        if (!user.isVerified()) {
            throw new ApiException("Email not verified");
        }

        if (!userService.checkPassword(user.getId(), password)) {
            throw new ApiException("Invalid credentials");
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refreshStr = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user.getId(), refreshStr, REFRESH_EXPIRY_DAYS);

        return new Authentication(accessToken, refreshStr, user);
    }

    @Override
    public void forgotPassword(String email) {
        userService.findByEmail(email).ifPresent(user -> {
            String code = generateRandom4Digits();
            otpService.createOtp(user.getId(), "RESET_PASSWORD", code, Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));

            UserProfileDTO userProfile = userProfileService.getProfileByUserId(user.getId());

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("NAME", userProfile.getName());
            placeholders.put("OTP_CODE", code);

//            emailService.sendTemplatedEmail(
//                    user.getEmail(),
//                    "password-reset",
//                    placeholders
//            );
        });
    }

    @Override
    public void verifyForgotPasswordOtp(String email, String code) {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user.getId(), "RESET_PASSWORD", code);
    }

    @Override
    public void resetPassword(String email, String otpCode, String newPassword) {
        UserDTO user = userService.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user.getId(), "RESET_PASSWORD", otpCode);
        userService.updatePassword(user.getId(), newPassword);
        otpService.markUsed(otp.getId());
    }

    @Override
    public Authentication loginWithGoogle(String idToken) {
        GoogleUserInfo googleInfo = socialAuthUtil.validateGoogleToken(idToken);

        String provider = "GOOGLE";
        String providerId = googleInfo.getSub();

        Optional<UserSocialAccount> socialOpt = socialAccountService.findByProviderAndProviderId(provider, providerId);
        UserDTO user = userService.findByEmail(googleInfo.getEmail()).orElse(null);
        if (user == null) {
            user = userService.createUser(googleInfo.getEmail(), null);
            userService.verifyUser(user.getId());
            userProfileService.createProfile(user.getId(), googleInfo.getName());
        }

        if(socialOpt.isEmpty())
            socialAccountService.linkSocialAccount(user.getId(), provider, providerId);

        if (googleInfo.getPicture() != null) {
            UserProfileDTO profile = userProfileService.getProfileByUserId(user.getId());
            if (profile.getProfilePhotoUrl() == null)
                userProfileService.updateProfilePhoto(profile.getId(), googleInfo.getPicture());
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user.getId(), refresh, REFRESH_EXPIRY_DAYS);

        return new Authentication(accessToken, refresh, user);
    }


    @Override
    public Authentication loginWithFacebook(String idToken) {
        FacebookUserInfo fbInfo = socialAuthUtil.validateFacebookToken(idToken);

        String provider = "FACEBOOK";
        String providerId = fbInfo.getId();

        Optional<UserSocialAccount> socialOpt = socialAccountService.findByProviderAndProviderId(provider, providerId);
        UserDTO user = userService.findByEmail(fbInfo.getEmail()).orElse(null);
        if (user == null) {
            user = userService.createUser(fbInfo.getEmail(), null);
            userService.verifyUser(user.getId());
            userProfileService.createProfile(user.getId(), fbInfo.getName());
        }

        if(socialOpt.isEmpty())
            socialAccountService.linkSocialAccount(user.getId(), provider, providerId);

        if (fbInfo.getPicture() != null) {
            UserProfileDTO profile = userProfileService.getProfileByUserId(user.getId());
            if (profile.getProfilePhotoUrl() == null)
                userProfileService.updateProfilePhoto(profile.getId(), fbInfo.getPicture());
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user.getId(), refresh, REFRESH_EXPIRY_DAYS);

        return new Authentication(accessToken, refresh, user);
    }

    @Override
    public Authentication refreshToken(String refreshToken) {
        RefreshToken refresh = refreshTokenService.validateRefreshToken(refreshToken);
        User user = refresh.getUser();

        String newRefresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.rotateRefreshToken(refresh.getId(), newRefresh);

        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());

        UserDTO userDTO = userService.findByEmail(user.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        return new Authentication(newAccessToken, newRefresh, userDTO);
    }


    private String generateRandom4Digits() {
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }
}