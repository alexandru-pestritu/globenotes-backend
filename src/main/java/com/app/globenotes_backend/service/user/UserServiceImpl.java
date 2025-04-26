package com.app.globenotes_backend.service.user;

import com.app.globenotes_backend.dto.user.UserDTO;
import com.app.globenotes_backend.dto.user.UserMapper;
import com.app.globenotes_backend.dto.userProfile.UserProfileDTO;
import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.service.email.EmailService;
import com.app.globenotes_backend.service.otp.OtpService;
import com.app.globenotes_backend.service.userProfile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.globenotes_backend.repository.UserRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;
    private final UserProfileService userProfileService;

    private final long OTP_EXPIRY_MINUTES = 10;

    @Override
    public UserDTO createUser(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email already in use");
        }
        User user = new User();
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
    public Optional<UserDTO> findById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDTO);
    }

    @Override
    public void updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        if (userRepository.existsByEmail(newEmail)) {
            throw new ApiException("Email already in use");
        }

        user.setEmail(newEmail);
        user.setIsVerified(false);
        userRepository.save(user);

        UserProfileDTO userProfile = userProfileService.getProfileByUserId(user.getId());

        String code = generateRandom4Digits();
        otpService.createOtp(user.getId(), "VERIFICATION", code, Instant.now().plus(Duration.ofMinutes(OTP_EXPIRY_MINUTES)));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("NAME", userProfile.getName());
        placeholders.put("OTP_CODE", code);

//        emailService.sendTemplatedEmail(
//                user.getEmail(),
//                "email-verification",
//                placeholders
//            );
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
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

    private String generateRandom4Digits() {
        int code = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(code);
    }
}
