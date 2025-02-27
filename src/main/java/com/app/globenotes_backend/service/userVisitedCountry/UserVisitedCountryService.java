package com.app.globenotes_backend.service.userVisitedCountry;

import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryDTO;
import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryDetailsDTO;

import java.util.List;

public interface UserVisitedCountryService {
    UserVisitedCountryDetailsDTO addUserVisitedCountry(UserVisitedCountryDTO userVisitedCountryDTO);
    void deleteUserVisitedCountry(Long userId, Long userVisitedCountryId);
    List<UserVisitedCountryDetailsDTO> getUserVisitedCountriesByUserId(Long userId);
}
