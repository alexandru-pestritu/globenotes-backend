package com.app.globenotes_backend.service.otp;

import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.model.OtpCode;
import com.app.globenotes_backend.model.User;
import com.app.globenotes_backend.repository.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpCodeRepository;

    @Override
    public OtpCode createOtp(User user, String type, String code, LocalDateTime expiresAt) {
        OtpCode otp = new OtpCode();
        otp.setUser(user);
        otp.setType(type);
        otp.setCode(code);
        otp.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        otp.setExpiresAt(expiresAt);
        return otpCodeRepository.save(otp);
    }

    @Override
    public OtpCode validateOtp(User user, String type, String code) {
        OtpCode otp = otpCodeRepository
                .findByUserIdAndTypeAndUsedAtIsNullAndExpiresAtAfter(user.getId(), type, LocalDateTime.now(ZoneOffset.UTC))
                .orElseThrow(() -> new ApiException("OTP invalid or expired"));

        if (!otp.getCode().equals(code)) {
            throw new ApiException("OTP mismatch");
        }
        return otp;
    }

    @Override
    public void markUsed(OtpCode otp) {
        otp.setUsedAt(LocalDateTime.now(ZoneOffset.UTC));
        otpCodeRepository.save(otp);
    }
}