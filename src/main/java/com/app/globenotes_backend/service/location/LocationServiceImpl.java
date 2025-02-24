package com.app.globenotes_backend.service.location;

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

    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location updateLocation(Long id, Location newData) {
        Location loc = locationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Location not found"));

        loc.setLatitude(newData.getLatitude());
        loc.setLongitude(newData.getLongitude());
        loc.setFormattedAddress(newData.getFormattedAddress());
        loc.setCity(newData.getCity());
        loc.setState(newData.getState());
        loc.setGooglePlaceId(newData.getGooglePlaceId());
        loc.setCountry(newData.getCountry());

        return locationRepository.save(loc);
    }

    @Override
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ApiException("Location not found");
        }
        locationRepository.deleteById(id);
    }
}