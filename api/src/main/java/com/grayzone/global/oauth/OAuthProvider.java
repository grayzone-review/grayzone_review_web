package com.grayzone.global.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum OAuthProvider {
  KAKAO,
  APPLE,
  ADMIN,
  INACTIVE;

  @JsonCreator
  public static OAuthProvider from(String name) {
    return switch (name.toLowerCase()) {
      case "kakao" -> KAKAO;
      case "apple" -> APPLE;
      default -> throw new UpException(UpError.OAUTH_UNSUPPORTED_PROVIDER);
    };
  }
}
