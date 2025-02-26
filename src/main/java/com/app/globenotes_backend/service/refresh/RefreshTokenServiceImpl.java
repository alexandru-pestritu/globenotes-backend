package com.app.globenotes_backend.service.refresh;

import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.entity.RefreshToken;
import com.app.globenotes_backend.entity.User;
import com.app.globenotes_backend.repository.RefreshTokenRepository;
import com.app.globenotes_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(Long userId, String token, long daysValid) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found"));

        RefreshToken refresh = RefreshToken.builder()
                .user(user)
                .token(token)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofDays(daysValid)))
                .build();
        return refreshTokenRepository.save(refresh);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refresh = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException("Refresh token not found"));
        if (refresh.getExpiresAt().isBefore(Instant.now())) {
            throw new ApiException("Refresh token expired");
        }
        return refresh;
    }

    @Override
    public RefreshToken rotateRefreshToken(Long refreshTokenId, String newToken) {
        RefreshToken refresh = refreshTokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new ApiException("Refresh token not found"));

        refresh.setToken(newToken);
        refresh.setCreatedAt(Instant.now());
        return refreshTokenRepository.save(refresh);
    }
}
