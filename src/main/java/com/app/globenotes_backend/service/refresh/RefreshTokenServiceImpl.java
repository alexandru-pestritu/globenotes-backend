package com.app.globenotes_backend.service.refresh;

import com.app.globenotes_backend.exception.ApiException;
import com.app.globenotes_backend.model.RefreshToken;
import com.app.globenotes_backend.model.User;
import com.app.globenotes_backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user, String token, long daysValid) {
        RefreshToken refresh = RefreshToken.builder()
                .user(user)
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(daysValid))
                .build();
        return refreshTokenRepository.save(refresh);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refresh = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException("Refresh token not found"));
        if (refresh.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ApiException("Refresh token expired");
        }
        return refresh;
    }

    @Override
    public RefreshToken rotateRefreshToken(RefreshToken refreshToken, String newToken, long daysValid) {
        refreshToken.setToken(newToken);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(daysValid));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void invalidateRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
