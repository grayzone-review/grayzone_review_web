package com.grayzone.global.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RefreshTokenUtils {
  private final PasswordEncoder passwordEncoder;
  private final SecureRandom secureRandom = new SecureRandom();

  public String createToken(String username) {
    String emailKey = encodeUsernameToKey(username);

    byte[] tokenBytes = new byte[16];
    secureRandom.nextBytes(tokenBytes);
    String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

    return String.format("%s-%s", emailKey, randomToken);
  }

  public String hashToken(String refreshToken) {
    return passwordEncoder.encode(refreshToken);
  }

  public boolean validateRefreshToken(String rawRefreshToken, String hashedRefreshToken) {
    return passwordEncoder.matches(rawRefreshToken, hashedRefreshToken);
  }

  public String encodeUsernameToKey(String email) {
    return Base64.getUrlEncoder().withoutPadding()
      .encodeToString(email.getBytes(StandardCharsets.UTF_8));
  }
}
