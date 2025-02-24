package com.app.globenotes_backend.controller;

import com.app.globenotes_backend.dto.response.HttpResponse;
import com.app.globenotes_backend.dto.authentication.Authentication;
import com.app.globenotes_backend.service.auth.AuthService;
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
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(
            @RequestParam @NotBlank @Size(min = 3, max = 50) String name,
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank @Size(min = 8) String password) {

        authService.register(name, email, password);
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
    public ResponseEntity<HttpResponse> resendOtp(
            @RequestParam @NotBlank @Email String email) {

        authService.resendOtp(email);
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
    public ResponseEntity<HttpResponse> verifyEmail(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank String code) {

        authService.verifyEmail(email, code);
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
    public ResponseEntity<HttpResponse> login(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank String password) {

        Authentication resp = authService.login(email, password);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
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
    public ResponseEntity<HttpResponse> forgotPassword(
            @RequestParam @NotBlank @Email String email) {

        authService.forgotPassword(email);
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
    public ResponseEntity<HttpResponse> verifyForgotPasswordOtp(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank String code) {

        authService.verifyForgotPasswordOtp(email, code);
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
    public ResponseEntity<HttpResponse> resetPassword(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank String otpCode,
            @RequestParam @NotBlank @Size(min = 8) String newPassword) {

        authService.resetPassword(email, otpCode, newPassword);
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
    public ResponseEntity<HttpResponse> loginWithGoogle(
            @RequestParam @NotBlank String idToken) {

        Authentication resp = authService.loginWithGoogle(idToken);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
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
    public ResponseEntity<HttpResponse> loginWithFacebook(
            @RequestParam @NotBlank String idToken) {

        Authentication resp = authService.loginWithFacebook(idToken);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
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
    public ResponseEntity<HttpResponse> refreshToken(
            @RequestParam @NotBlank String refreshToken) {

        Authentication resp = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(Instant.now().toString())
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
