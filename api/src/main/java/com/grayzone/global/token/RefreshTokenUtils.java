package com.grayzone.global.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtils {
  private final SecureRandom secureRandom = new SecureRandom();

  public String createToken(long userId) {
    byte[] tokenBytes = new byte[32];
    secureRandom.nextBytes(tokenBytes);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }
}