package com.grayzone.global.oauth.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AppleUtils {

  @Value(("${apple.key-id}"))
  private String appleKeyId;

  @Value("${apple.client-id}")
  private String appleClientId;

  @Value("${apple.issuer-uri}")
  private String appleAudience;

  @Value("${apple.up-issuer-id}")
  private String issuerId;

  @Value("${apple.jwt-secret}")
  private String appleJwtSecret;

  public String createClientSecret() {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + 3600000);
    String jwtSecret = appleJwtSecret.replace("\\n", "\n");

    return Jwts.builder()
      .header().add("alg", "ES256").and()
      .header().add("kid", appleKeyId).and()
      .issuer(issuerId)
      .subject(appleClientId)
      .audience().add(appleAudience).and()
      .issuedAt(now)
      .expiration(expiration)
      .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
      .compact();
  }
}
