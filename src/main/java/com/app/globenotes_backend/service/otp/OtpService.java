package com.app.globenotes_backend.service.otp;

import com.app.globenotes_backend.entity.OtpCode;
import com.app.globenotes_backend.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;

public interface OtpService {

    OtpCode createOtp(Long userId, String type, String code, Instant expiresAt);

    OtpCode validateOtp(Long userId, String type, String code);

    void markUsed(Long otpId);
}
