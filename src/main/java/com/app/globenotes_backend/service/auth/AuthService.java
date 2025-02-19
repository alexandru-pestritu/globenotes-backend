package com.app.globenotes_backend.service.auth;

import com.app.globenotes_backend.dto.request.*;
import com.app.globenotes_backend.dto.response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest request);

    void resendOtp(ResendOtpRequest request);

    void verifyEmail(OtpVerifyRequest request);

    LoginResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void verifyForgotPasswordOtp(OtpVerifyRequest request);

    void resetPassword(ResetPasswordRequest request);

    LoginResponse loginWithGoogle(SocialLoginRequest request);

    LoginResponse loginWithFacebook(SocialLoginRequest request);

    LoginResponse refreshToken(RefreshRequest request);
}