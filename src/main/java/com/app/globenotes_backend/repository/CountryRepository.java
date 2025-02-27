package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findByContinentId(Long continentId);
    Optional<Country> findByIsoCode(String isoCode);
}
