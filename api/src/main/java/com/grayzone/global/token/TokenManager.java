package com.grayzone.global.token;

import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenManager {
  private static final long ACCESS_TOKEN_VALIDITY_TIME = 1000L * 60 * 60;
  private static final long REFRESH_TOKEN_VALIDITY_TIME = 1000L * 60 * 60 * 24 * 7;

  private final JWTUtils jwtUtils;
  private final RefreshTokenUtils refreshTokenUtils;
  private final RedisTemplate<String, Object> redisTemplate;

  public String createAccessToken(long userId, long validityTime) {
    return jwtUtils.createToken(userId, validityTime);
  }

  public String createRefreshToken(long userId) {
    String refreshToken = refreshTokenUtils.createToken(userId);
    redisTemplate.opsForValue().set(
      refreshToken,
      userId,
      REFRESH_TOKEN_VALIDITY_TIME,
      TimeUnit.MILLISECONDS
    );

    return refreshToken;
  }

  public TokenPair createTokenPair(long userId) {
    return new TokenPair(createAccessToken(userId, ACCESS_TOKEN_VALIDITY_TIME), createRefreshToken(userId));
  }

  public TokenPair createTokenPair(long userId, long validityTime) {
    return new TokenPair(createAccessToken(userId, validityTime), createRefreshToken(userId));
  }

  public boolean validateAccessToken(String token) {
    return jwtUtils.validateToken(token);
  }

  public boolean validateRefreshToken(String token) {
    return redisTemplate.opsForValue().get(token) != null;
  }

  public String parseSubject(String token) {
    return jwtUtils.parseToken(token).getSubject();
  }

  public Long parseUserIdFromRefreshToken(String token) {
    Object userId = redisTemplate.opsForValue().get(token);

    if (!(userId instanceof Integer)) {
      throw new UpException(UpError.REFRESH_TOKEN_INVALID);
    }

    return ((Integer) userId).longValue();
  }

  public void invalidateRefreshToken(String token) {
    redisTemplate.delete(token);
  }
}
