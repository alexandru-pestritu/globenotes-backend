package com.app.globenotes_backend.service.auth;

import com.app.globenotes_backend.dto.request.*;
import com.app.globenotes_backend.dto.response.LoginResponse;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.model.OtpCode;
import com.app.globenotes_backend.model.RefreshToken;
import com.app.globenotes_backend.model.User;
import com.app.globenotes_backend.model.UserSocialAccount;
import com.app.globenotes_backend.service.otp.OtpService;
import com.app.globenotes_backend.service.refresh.RefreshTokenService;
import com.app.globenotes_backend.service.social.SocialAccountService;
import com.app.globenotes_backend.service.user.UserService;
import com.app.globenotes_backend.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final SocialAccountService socialAccountService;
    private final JwtUtils jwtUtils;

    @Override
    public void register(RegisterRequest request) {
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPassword());

        String code = generateRandom4Digits();
        otpService.createOtp(user, "VERIFICATION", code, LocalDateTime.now().plusMinutes(10));

        // TODO: send email with 'code'
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
        RefreshToken refresh = refreshTokenService.createRefreshToken(user, refreshStr, 365L);

        return new LoginResponse(accessToken, refreshStr);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        userService.findByEmail(request.getEmail()).ifPresent(user -> {
            String code = generateRandom4Digits();
            otpService.createOtp(user, "RESET_PASSWORD", code, LocalDateTime.now().plusMinutes(10));
            // TODO: send email with code
        });
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
        //TODO: Google login implementation
        throw new UnsupportedOperationException("Implementation for Google not done yet.");
    }

    @Override
    public LoginResponse loginWithFacebook(SocialLoginRequest request) {
        //TODO: Facebook login implementation
        throw new UnsupportedOperationException("Implementation for Facebook not done yet.");
    }

    @Override
    public LoginResponse refreshToken(RefreshRequest request) {
        RefreshToken refresh = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = refresh.getUser();

        String newRefresh = jwtUtils.generateRefreshTokenString();
        refreshTokenService.rotateRefreshToken(refresh, newRefresh, 365L);

        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail());

        return new LoginResponse(newAccessToken, newRefresh);
    }


    private String generateRandom4Digits() {
        // ex
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }
}