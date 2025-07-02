package com.grayzone.global.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OAuthProvider {
  KAKAO,
  APPLE;

  @JsonCreator
  public static OAuthProvider from(String name) {
    return switch (name.toLowerCase()) {
      case "kakao" -> KAKAO;
      case "apple" -> APPLE;
      default -> throw new IllegalArgumentException("Unknown OAuth provider: " + name);
    };
  }
}
