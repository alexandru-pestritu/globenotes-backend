package com.app.globenotes_backend.service.refresh;

import com.app.globenotes_backend.entity.RefreshToken;
import com.app.globenotes_backend.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId, String token, long daysValid);

    RefreshToken validateRefreshToken(String token);

    RefreshToken rotateRefreshToken(Long refreshTokenId, String newToken);
}
