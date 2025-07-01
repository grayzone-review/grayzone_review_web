package com.grayzone.global.oauth.apple;

import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.security.PublicKey;

@Component
public class AppleOAuthUserInfoProvider implements OAuthUserInfoProvider {
  private final RestClient restClient = RestClient.create();

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

    JwsHeader header = Jwts.parser()
      .build()
      .parseSignedClaims(token)
      .getHeader();

    PublicKey publicKey = applePublicKeys
      .getMatchedPublicKey(header.getAlgorithm(), header.getKeyId());

    Claims payload = Jwts.parser()
      .verifyWith(publicKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();

    verifyAppleIdTokenClaims(payload);
    String email = payload.get("email", String.class);

    return new OAuthUserInfo(OAuthProvider.APPLE, email);
  }

  private void verifyAppleIdTokenClaims(Claims payload) {
    if (!payload.getIssuer().equals(appleIssuerUri)) {
      throw new IllegalArgumentException("Invalid Token");
    }

    if (!payload.getAudience().equals(appleClientId)) {
      throw new IllegalArgumentException("Invalid Token");
    }
  }
}
