package com.app.globenotes_backend.service.auth;

import com.app.globenotes_backend.dto.authentication.Authentication;

public interface AuthService {

    void register(String name, String email, String password);

    void resendOtp(String email);

    void verifyEmail(String email, String code);

    Authentication login(String email, String password);

    void forgotPassword(String email);

    void verifyForgotPasswordOtp(String email, String code);

    void resetPassword(String email, String otpCode, String newPassword);

    Authentication loginWithGoogle(String idToken);

    Authentication loginWithFacebook(String idToken);

    Authentication refreshToken(String refreshToken);
}
