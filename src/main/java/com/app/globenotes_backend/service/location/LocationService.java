package com.app.globenotes_backend.service.location;

import com.app.globenotes_backend.dto.location.LocationDetailsDTO;
import com.app.globenotes_backend.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    Location createLocation(LocationDetailsDTO location);

}
