package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);
    Optional<UserProfile> findByUserIdAndUpdatedAtAfter(Long userId, Instant lastSync);
}
