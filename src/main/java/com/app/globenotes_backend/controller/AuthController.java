package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.request.*;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.response.LoginResponse;
import com.app.globenotes_backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("User created successfully, check email for OTP.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<HttpResponse> verifyEmail(@RequestBody OtpVerifyRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Email verified successfully.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken()
                        ))
                        .build()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("If email is valid, we sent an OTP for resetting password.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Password has been reset.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/login/google")
    public ResponseEntity<HttpResponse> loginWithGoogle(@RequestBody SocialLoginRequest request) {
        LoginResponse resp = authService.loginWithGoogle(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Login with Google Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken()
                        ))
                        .build()
        );
    }

    @PostMapping("/login/facebook")
    public ResponseEntity<HttpResponse> loginWithFacebook(@RequestBody SocialLoginRequest request) {
        LoginResponse resp = authService.loginWithFacebook(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Login with Facebook Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken()
                        ))
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<HttpResponse> refreshToken(@RequestBody RefreshRequest request) {
        LoginResponse resp = authService.refreshToken(request);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .message("Token refreshed successfully.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken()
                        ))
                        .build()
        );
    }
}