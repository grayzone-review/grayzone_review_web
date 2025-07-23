package com.grayzone.global.oauth.apple;

import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.Getter;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;

@Getter
public class ApplePublicKeys {
  private List<JWKSet> keys;

  public PublicKey getMatchedPublicKey(String alg, String kid) {
    if (keys == null) {
      throw new UpException(UpError.SERVER_ERROR);
    }

    JWKSet jwkSet = keys.stream()
      .filter(key -> key.isMatched(alg, kid))
      .findFirst()
      .orElseThrow(() -> new UpException(UpError.OAUTH_INVALID_TOKEN));

    return jwkSet.getPublicKey();
  }

  @Getter
  static class JWKSet {
    private String kty;
    private String kid;
    private String use;
    private String n;
    private String e;
    private String alg;

    boolean isMatched(String alg, String kid) {
      return alg.equals(this.alg) && kid.equals(this.kid);
    }

    PublicKey getPublicKey() {
      byte[] modulusBytes = Base64.getUrlDecoder().decode(this.n);
      byte[] exponentBytes = Base64.getUrlDecoder().decode(this.e);

      BigInteger modulus = new BigInteger(1, modulusBytes);
      BigInteger exponent = new BigInteger(1, exponentBytes);

      RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

      try {
        KeyFactory keyFactory = KeyFactory.getInstance(this.kty);

        return keyFactory.generatePublic(keySpec);
      } catch (Exception e) {
        throw new UpException(UpError.OAUTH_INVALID_TOKEN);
      }
    }
  }
}
