package com.grayzone.global.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JWTUtils {

  @Value("${jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(long userId, long expirationTime) {
    Claims claims = Jwts.claims()
      .subject(String.valueOf(userId))
      .build();

    Date now = new Date();
    Date validity = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
      .claims(claims)
      .issuedAt(now)
      .expiration(validity)
      .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .build()
        .parseSignedClaims(token);

      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error(e.getMessage());
      return false;
    }
  }

  public Claims parseToken(String token) {
    return Jwts.parser()
      .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}
