package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByIdAndUpdatedAtAfter(Long userId, Instant lastSync);
}