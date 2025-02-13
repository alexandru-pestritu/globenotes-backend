package com.app.globenotes_backend.service.location;

import com.app.globenotes_backend.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    Location createLocation(Location location);

    Optional<Location> getLocationById(Long id);

    List<Location> getAllLocations();

    Location updateLocation(Long id, Location newData);

    void deleteLocation(Long id);
}
