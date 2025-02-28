package com.app.globenotes_backend.service.userVisitedCountry;

import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryDTO;
import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryDetailsDTO;
import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryMapper;
import com.app.globenotes_backend.entity.Country;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.entity.UserVisitedCountry;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.repository.CountryRepository;
import com.app.globenotes_backend.repository.UserRepository;
import com.app.globenotes_backend.repository.UserVisitedCountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVisitedCountryServiceImpl implements UserVisitedCountryService{
    private final UserVisitedCountryRepository userVisitedCountryRepository;
    private final UserVisitedCountryMapper userVisitedCountryMapper;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    @Override
    public UserVisitedCountryDetailsDTO addUserVisitedCountry(UserVisitedCountryDTO userVisitedCountryDTO) {
        Optional<UserVisitedCountry> existing = userVisitedCountryRepository
                .findByCountryIdAndUserId(userVisitedCountryDTO.getCountryId(), userVisitedCountryDTO.getUserId());

        if (existing.isPresent()) {
            return userVisitedCountryMapper.toDetailsDTO(existing.get());
        }

        User user = userRepository.findById(userVisitedCountryDTO.getUserId())
                .orElseThrow(() -> new ApiException("User not found"));

        Country country = countryRepository.findById(userVisitedCountryDTO.getCountryId())
                .orElseThrow(() -> new ApiException("Country not found"));


        UserVisitedCountry userVisitedCountry = userVisitedCountryMapper
                .toEntity(userVisitedCountryDTO);

        userVisitedCountry.setCountry(country);
        userVisitedCountry.setUser(user);

        userVisitedCountry = userVisitedCountryRepository.save(userVisitedCountry);

        return userVisitedCountryMapper.toDetailsDTO(userVisitedCountry);
    }


    @Override
    public void deleteUserVisitedCountry(Long userId, Long userVisitedCountryId) {
        Optional<UserVisitedCountry> userVisitedCountry = userVisitedCountryRepository.findByUserIdAndId(userId, userVisitedCountryId);

        if(userVisitedCountry.isPresent()){
            userVisitedCountry.get().setIsDeleted(true);
            userVisitedCountryRepository.save(userVisitedCountry.get());
        }
        else {
            throw new ApiException("User visited country not found");
        }
    }

    @Override
    public List<UserVisitedCountryDetailsDTO> getUserVisitedCountriesByUserId(Long userId) {
        return userVisitedCountryRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(userVisitedCountryMapper::toDetailsDTO)
                .collect(Collectors.toList());
    }
}
