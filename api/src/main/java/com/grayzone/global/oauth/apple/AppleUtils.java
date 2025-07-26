package com.grayzone.global.oauth.apple;

import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
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
    Date expiration = new Date(now.getTime() + 60L * 60 * 24 * 30 * 2 * 1000);

    return Jwts.builder()
      .header().add("alg", "ES256").and()
      .header().add("kid", appleKeyId).and()
      .issuer(issuerId)
      .subject(appleClientId)
      .audience().add(appleAudience).and()
      .issuedAt(now)
      .expiration(expiration)
      .signWith(getPrivateKey())
      .compact();
  }

  private PrivateKey getPrivateKey() {
    String jwtSecret = appleJwtSecret.replace("\\n", "\n");

    try {
      byte[] encoded = Base64.decodeBase64(jwtSecret);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      return keyFactory.generatePrivate(keySpec);
    } catch (Exception e) {
      throw new UpException(UpError.SERVER_ERROR);
    }
  }
}




