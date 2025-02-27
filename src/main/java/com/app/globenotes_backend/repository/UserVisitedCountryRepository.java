package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.UserVisitedCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVisitedCountryRepository extends JpaRepository<UserVisitedCountry, Long> {
}
