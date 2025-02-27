package com.app.globenotes_backend.service.location;

import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import com.app.globenotes_backend.dto.location.LocationMapper;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.Location;
import com.app.globenotes_backend.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


    @Override
    public Location createLocation(LocationDetailsDTO location) {
        Optional<Location> existing = locationRepository.findLocationByLongitudeAndLatitude(location.getLongitude(), location.getLatitude());

        return existing.orElseGet(() -> locationRepository.save(locationMapper.toEntityFromDetails(location)));
    }
}