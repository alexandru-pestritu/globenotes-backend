package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.UserVisitedCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVisitedCountryRepository extends JpaRepository<UserVisitedCountry, Long> {
    Optional<UserVisitedCountry> findByCountryIdAndUserId(Long countryId, Long userId);
    Optional<UserVisitedCountry> findByUserIdAndId(Long userId, Long userVisitedCountryId);
    List<UserVisitedCountry> findByUserIdAndIsDeletedFalse(Long userId);
    List<UserVisitedCountry> findByUserIdAndVisitedAtAfter(Long userId, Instant lastSync);
}
