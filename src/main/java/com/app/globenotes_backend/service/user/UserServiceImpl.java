package com.app.globenotes_backend.service.user;

import com.app.globenotes_backend.dto.user.UserDTO;
import com.app.globenotes_backend.dto.user.UserMapper;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.globenotes_backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email already in use");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        if(rawPassword != null)
            user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsVerified(false);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDTO);
    }

    @Override
    public void verifyUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public boolean checkPassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void updatePassword(Long userId, String newRawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }
}
