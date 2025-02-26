package com.app.globenotes_backend.service.country;

import com.app.globenotes_backend.dto.country.CountryDTO;
import com.app.globenotes_backend.dto.country.CountryMapper;
import com.app.globenotes_backend.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;


    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(countryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
