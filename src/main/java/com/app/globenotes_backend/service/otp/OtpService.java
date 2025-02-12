package com.app.globenotes_backend.service.otp;

import com.app.globenotes_backend.model.OtpCode;
import com.app.globenotes_backend.model.User;

import java.time.LocalDateTime;

public interface OtpService {

    OtpCode createOtp(User user, String type, String code, LocalDateTime expiresAt);

    OtpCode validateOtp(User user, String type, String code);

    void markUsed(OtpCode otp);
}
