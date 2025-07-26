package com.grayzone.global.oauth.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Date;

@Slf4j
@Component
public class AppleOAuthTokenGenerator {
  private final RestClient restClient = RestClient.create();

  @Value("${apple.generate-token-uri}")
  private String appleGenerateTokenUri;

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

  public void generateAppleToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", appleClientId);
    body.add("client_secret", createClientSecret());
    body.add("code", code);

    restClient.post()
      .uri(appleGenerateTokenUri)
      .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .body(body)
      .retrieve()
      .body(String.class);
  }

  private String createClientSecret() {
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
