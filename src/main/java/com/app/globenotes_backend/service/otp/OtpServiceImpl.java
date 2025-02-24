package com.app.globenotes_backend.service.otp;

import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.OtpCode;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.repository.OtpCodeRepository;
import com.app.globenotes_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final UserRepository userRepository;

    @Override
    public OtpCode createOtp(Long userId, String type, String code, LocalDateTime expiresAt) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        List<OtpCode> oldOtpCodes = otpCodeRepository
                .findAllActiveByUserAndType(userId, type, LocalDateTime.now(ZoneOffset.UTC));

        for (OtpCode old : oldOtpCodes) {
            old.setUsedAt(LocalDateTime.now(ZoneOffset.UTC));
            otpCodeRepository.save(old);
        }

        OtpCode otp = new OtpCode();
        otp.setUser(user);
        otp.setType(type);
        otp.setCode(code);
        otp.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        otp.setExpiresAt(expiresAt);
        return otpCodeRepository.save(otp);
    }

    @Override
    public OtpCode validateOtp(Long userId, String type, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        OtpCode otp = otpCodeRepository
                .findByUserIdAndTypeAndUsedAtIsNullAndExpiresAtAfter(user.getId(), type, LocalDateTime.now(ZoneOffset.UTC))
                .orElseThrow(() -> new ApiException("OTP invalid or expired"));

        if (!otp.getCode().equals(code)) {
            throw new ApiException("OTP mismatch");
        }
        return otp;
    }

    @Override
    public void markUsed(Long otpId) {
        OtpCode otp = otpCodeRepository.findById(otpId)
                .orElseThrow(() -> new ApiException("OTP not found"));

        otp.setUsedAt(LocalDateTime.now(ZoneOffset.UTC));
        otpCodeRepository.save(otp);
    }
}