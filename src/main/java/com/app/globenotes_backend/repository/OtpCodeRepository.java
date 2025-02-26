package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByUserIdAndTypeAndUsedAtIsNullAndExpiresAtAfter(
            Long userId,
            String type,
            Instant now
    );

    @Query("SELECT o FROM OtpCode o " +
            "WHERE o.user.id = :userId " +
            "AND o.type = :type " +
            "AND o.usedAt IS NULL " +
            "AND o.expiresAt > :now")
    List<OtpCode> findAllActiveByUserAndType(Long userId, String type, Instant now);
}