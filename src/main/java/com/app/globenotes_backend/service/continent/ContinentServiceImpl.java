package com.app.globenotes_backend.service.continent;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import com.app.globenotes_backend.dto.continent.ContinentDetailsDTO;
import com.app.globenotes_backend.dto.continent.ContinentMapper;
import com.app.globenotes_backend.dto.country.CountryDTO;
import com.app.globenotes_backend.dto.country.CountryMapper;
import com.app.globenotes_backend.repository.ContinentRepository;
import com.app.globenotes_backend.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContinentServiceImpl implements ContinentService{
    private final ContinentRepository continentRepository;
    private final ContinentMapper continentMapper;
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<ContinentDTO> getAllContinents() {
        return continentRepository.findAll()
                .stream()
                .map(continentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContinentDetailsDTO> getAllContinentsWithCountries() {
        return continentRepository.findAll()
                .stream()
                .map(continent -> {
                    List<CountryDTO> countries = countryRepository.findByContinentId(continent.getId())
                            .stream()
                            .map(countryMapper::toDTO)
                            .collect(Collectors.toList());
                    ContinentDetailsDTO continentDetailsDTO = continentMapper.toDetailsDTO(continent);
                    continentDetailsDTO.setCountries(countries);
                    return continentDetailsDTO;
                })
                .collect(Collectors.toList());
    }
}
