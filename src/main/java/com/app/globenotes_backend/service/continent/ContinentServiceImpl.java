package com.app.globenotes_backend.service.continent;

import com.app.globenotes_backend.dto.continent.ContinentDTO;
import com.app.globenotes_backend.dto.continent.ContinentMapper;
import com.app.globenotes_backend.repository.ContinentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContinentServiceImpl implements ContinentService{
    private final ContinentRepository continentRepository;
    private final ContinentMapper continentMapper;

    @Override
    public List<ContinentDTO> getAllContinents() {
        return continentRepository.findAll()
                .stream()
                .map(continentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
