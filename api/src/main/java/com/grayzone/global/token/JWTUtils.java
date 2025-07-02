package com.grayzone.global.token;

import com.grayzone.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JWTUtils {

  @Value("${jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(User user, long expirationTime) {
    Long userId = user.getId();
    String username = user.getUsername();

    Claims claims = Jwts.claims()
      .subject(String.valueOf(userId))
      .build();
    claims.put("username", username);

    Date now = new Date();
    Date validity = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
      .claims(claims)
      .issuedAt(now)
      .expiration(validity)
      .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
      .compact();
  }
}
