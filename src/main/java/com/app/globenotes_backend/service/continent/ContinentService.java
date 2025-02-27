package com.app.globenotes_backend.service.continent;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import com.app.globenotes_backend.dto.continent.ContinentDetailsDTO;

import java.util.List;

public interface ContinentService {
    List<ContinentDTO> getAllContinents();
    List<ContinentDetailsDTO> getAllContinentsWithCountries();
}
