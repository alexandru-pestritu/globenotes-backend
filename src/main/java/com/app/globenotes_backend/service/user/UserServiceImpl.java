package com.app.globenotes_backend.service.user;

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

    @Override
    public User createUser(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email already in use");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        if(rawPassword != null)
            user.setPassword(passwordEncoder.encode(rawPassword));
        user.setIsVerified(false);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void verifyUser(User user) {
        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void updatePassword(User user, String newRawPassword) {
        user.setPassword(passwordEncoder.encode(newRawPassword));
        userRepository.save(user);
    }
}
