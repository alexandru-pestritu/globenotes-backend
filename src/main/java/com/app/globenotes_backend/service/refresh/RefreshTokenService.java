package com.app.globenotes_backend.service.refresh;

import com.app.globenotes_backend.entity.RefreshToken;
import com.app.globenotes_backend.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user, String token, long daysValid);

    RefreshToken validateRefreshToken(String token);

    RefreshToken rotateRefreshToken(RefreshToken refreshToken, String newToken, long daysValid);

    void invalidateRefreshToken(RefreshToken refreshToken);
}
