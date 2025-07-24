package com.grayzone.global.oauth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
@Component
public class AppleOAuthUserInfoProvider implements OAuthUserInfoProvider {
  private final RestClient restClient = RestClient.create();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${apple.public-key-uri}")
  private String applePublicKeyURI;
  @Value("${apple.client-id}")
  private String appleClientId;
  @Value("${apple.issuer-uri}")
  private String appleIssuerUri;

  @Override
  public boolean support(OAuthProvider provider) {
    return provider == OAuthProvider.APPLE;
  }

  @Override
  public OAuthUserInfo parse(String token) {
    ApplePublicKeys applePublicKeys = restClient.get()
      .uri(applePublicKeyURI)
      .retrieve()
      .body(ApplePublicKeys.class);

    AppleJWTHeader appleJWTHeader = parseJWTHeader(token);

    PublicKey publicKey = applePublicKeys
      .getMatchedPublicKey(appleJWTHeader.getAlg(), appleJWTHeader.getKid());

    try {
      Claims payload = Jwts.parser()
        .verifyWith(publicKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

      verifyAppleIdTokenClaims(payload);
      String email = payload.get("email", String.class);
      String oAuthid = payload.get("sub", String.class);

      return new OAuthUserInfo(OAuthProvider.APPLE, email, oAuthid);
    } catch (Exception e) {
      throw new UpException(UpError.OAUTH_INVALID_TOKEN);
    }
  }

  private void verifyAppleIdTokenClaims(Claims payload) {
    if (!payload.getIssuer().equals(appleIssuerUri)) {
      throw new UpException(UpError.OAUTH_INVALID_TOKEN);
    }

    if (!payload.getAudience().contains(appleClientId)) {
      throw new UpException(UpError.OAUTH_INVALID_TOKEN);
    }
  }

  private AppleJWTHeader parseJWTHeader(String token) {
    String[] parts = token.split("\\.");

    try {
      String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
      return objectMapper.readValue(headerJson, AppleJWTHeader.class);
    } catch (JsonProcessingException e) {
      throw new UpException(UpError.OAUTH_INVALID_TOKEN);
    }
  }
}
