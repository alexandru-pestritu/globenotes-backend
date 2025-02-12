package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByUserIdAndTypeAndUsedAtIsNullAndExpiresAtAfter(
            Long userId,
            String type,
            LocalDateTime now
    );
}