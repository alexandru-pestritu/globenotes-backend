package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByLongitudeAndLatitude(Double longitude, Double latitude);
}