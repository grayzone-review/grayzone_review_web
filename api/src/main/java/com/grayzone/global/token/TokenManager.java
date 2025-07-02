package com.grayzone.global.token;

import com.grayzone.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {
  private final long ACCESS_TOKEN_VALIDITY_TIME = 1000L * 60 * 60;

  private final JWTUtils jwtUtils;
  private final RefreshTokenUtils refreshTokenUtils;

  public String createAccessToken(User user) {
    return jwtUtils.createToken(user, ACCESS_TOKEN_VALIDITY_TIME);
  }

  public String createRefreshToken(User user) {
    return refreshTokenUtils.createToken(user);
  }
}
