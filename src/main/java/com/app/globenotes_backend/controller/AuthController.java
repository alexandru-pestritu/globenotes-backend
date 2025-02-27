package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.request.*;
import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.authentication.Authentication;
import com.app.globenotes_backend.service.auth.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("User created successfully, check email for OTP.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/register/resend-otp")
    public ResponseEntity<HttpResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request.getEmail());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("We sent another OTP to your email.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<HttpResponse> verifyEmail(@Valid @RequestBody OtpVerifyRequest request) {
        authService.verifyEmail(request.getEmail(), request.getCode());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Email verified successfully.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication resp = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Login Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken(),
                                "user", resp.getUser()
                        ))
                        .build()
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("If email is valid, we sent an OTP for resetting password.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<HttpResponse> verifyForgotPasswordOtp(@Valid @RequestBody OtpVerifyRequest request) {
        authService.verifyForgotPasswordOtp(request.getEmail(), request.getCode());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("OTP verified successfully.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<HttpResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getOtpCode(), request.getNewPassword());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Password has been reset.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/login/google")
    public ResponseEntity<HttpResponse> loginWithGoogle(@Valid @RequestBody SocialLoginRequest request) {
        Authentication resp = authService.loginWithGoogle(request.getIdToken());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Login with Google Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken(),
                                "user", resp.getUser()
                        ))
                        .build()
        );
    }

    @PostMapping("/login/facebook")
    public ResponseEntity<HttpResponse> loginWithFacebook(@Valid @RequestBody SocialLoginRequest request) {
        Authentication resp = authService.loginWithFacebook(request.getIdToken());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Login with Facebook Success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken(),
                                "user", resp.getUser()
                        ))
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<HttpResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        Authentication resp = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
                        .message("Token refreshed successfully.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of(
                                "access_token", resp.getAccessToken(),
                                "refresh_token", resp.getRefreshToken(),
                                "user", resp.getUser()
                        ))
                        .build()
        );
    }
}
