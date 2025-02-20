package com.app.globenotes_backend.service.auth;

import com.app.globenotes_backend.dto.request.*;
import com.app.globenotes_backend.dto.response.LoginResponse;
import com.app.globenotes_backend.dto.social.FacebookUserInfo;
import com.app.globenotes_backend.dto.social.GoogleUserInfo;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.model.*;
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
    public void register(RegisterRequest request) {
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPassword());
        userProfileService.createProfile(user);

        String code = generateRandom4Digits();
        otpService.createOtp(user, "VERIFICATION", code, LocalDateTime.now(ZoneOffset.UTC).plusMinutes(OTP_EXPIRY_MINUTES));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("NAME", user.getName());
        placeholders.put("OTP_CODE", code);

//        emailService.sendTemplatedEmail(
//                user.getEmail(),
//                "email-verification",
//                placeholders
//        );
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        if (user.getIsVerified()) {
            throw new ApiException("Email already verified");
        }

        String code = generateRandom4Digits();
        otpService.createOtp(user, "VERIFICATION", code, LocalDateTime.now(ZoneOffset.UTC).plusMinutes(OTP_EXPIRY_MINUTES));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("NAME", user.getName());
        placeholders.put("OTP_CODE", code);

//        emailService.sendTemplatedEmail(
//                user.getEmail(),
//                "email-verification",
//                placeholders
//            );
        }


    @Override
    public void verifyEmail(OtpVerifyRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user, "VERIFICATION", request.getCode());
        userService.verifyUser(user);
        otpService.markUsed(otp);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        if (!user.getIsVerified()) {
            throw new ApiException("Email not verified");
        }

        if (!userService.checkPassword(user, request.getPassword())) {
            throw new ApiException("Invalid credentials");
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refreshStr = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user, refreshStr, REFRESH_EXPIRY_DAYS);

        return new LoginResponse(accessToken, refreshStr);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        userService.findByEmail(request.getEmail()).ifPresent(user -> {
            String code = generateRandom4Digits();
            otpService.createOtp(user, "RESET_PASSWORD", code, LocalDateTime.now(ZoneOffset.UTC).plusMinutes(OTP_EXPIRY_MINUTES));

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("NAME", user.getName());
            placeholders.put("OTP_CODE", code);

//            emailService.sendTemplatedEmail(
//                    user.getEmail(),
//                    "password-reset",
//                    placeholders
//            );
        });
    }

    @Override
    public void verifyForgotPasswordOtp(OtpVerifyRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user, "RESET_PASSWORD", request.getCode());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));
        OtpCode otp = otpService.validateOtp(user, "RESET_PASSWORD", request.getOtpCode());
        userService.updatePassword(user, request.getNewPassword());
        otpService.markUsed(otp);
    }

    @Override
    public LoginResponse loginWithGoogle(SocialLoginRequest request) {
        GoogleUserInfo googleInfo = socialAuthUtil.validateGoogleToken(request.getIdToken());

        String provider = "GOOGLE";
        String providerId = googleInfo.getSub();

        Optional<UserSocialAccount> socialOpt = socialAccountService.findByProviderAndProviderId(provider, providerId);
        User user;
        if (socialOpt.isPresent()) {
            user = socialOpt.get().getUser();
        } else {
            user = userService.findByEmail(googleInfo.getEmail()).orElse(null);
            if (user == null) {
                user = userService.createUser(googleInfo.getName(), googleInfo.getEmail(), null);
                userService.verifyUser(user);
            }
            socialAccountService.linkSocialAccount(user, provider, providerId);

            if (googleInfo.getPicture() != null) {
                UserProfile profile = userProfileService.getProfileByUserId(user.getId())
                        .orElseThrow(() -> new ApiException("UserProfile not found."));
                if(profile.getProfilePhotoUrl() == null)
                    userProfileService.updateProfilePhoto(profile.getId(), googleInfo.getPicture());
            }
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user, refresh, REFRESH_EXPIRY_DAYS);

        return new LoginResponse(accessToken, refresh);
    }


    @Override
    public LoginResponse loginWithFacebook(SocialLoginRequest request) {
        FacebookUserInfo fbInfo = socialAuthUtil.validateFacebookToken(request.getIdToken());

        String provider = "FACEBOOK";
        String providerId = fbInfo.getId();

        Optional<UserSocialAccount> socialOpt = socialAccountService.findByProviderAndProviderId(provider, providerId);
        User user;
        if (socialOpt.isPresent()) {
            user = socialOpt.get().getUser();
        } else {
            user = userService.findByEmail(fbInfo.getEmail()).orElse(null);
            if (user == null) {
                user = userService.createUser(fbInfo.getName(), fbInfo.getEmail(), null);
                userService.verifyUser(user);
            }
            socialAccountService.linkSocialAccount(user, provider, providerId);

            if (fbInfo.getPicture() != null) {
                UserProfile profile = userProfileService.getProfileByUserId(user.getId())
                        .orElseThrow(() -> new ApiException("UserProfile not found."));
                if(profile.getProfilePhotoUrl() == null)
                    userProfileService.updateProfilePhoto(profile.getId(), fbInfo.getPicture());
            }
        }

        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());
        String refresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.createRefreshToken(user, refresh, REFRESH_EXPIRY_DAYS);

        return new LoginResponse(accessToken, refresh);
    }

    @Override
    public LoginResponse refreshToken(RefreshRequest request) {
        RefreshToken refresh = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = refresh.getUser();

        String newRefresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.rotateRefreshToken(refresh, newRefresh, REFRESH_EXPIRY_DAYS);

        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());

        return new LoginResponse(newAccessToken, newRefresh);
    }


    private String generateRandom4Digits() {
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }
}