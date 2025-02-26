package com.app.globenotes_backend.service.country;

import com.app.globenotes_backend.dto.country.CountryDTO;

import java.util.List;

public interface CountryService {
    List<CountryDTO> getAllCountries();
}
