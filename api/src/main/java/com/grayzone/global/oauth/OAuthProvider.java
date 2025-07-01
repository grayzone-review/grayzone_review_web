package com.grayzone.global.oauth;

public enum OAuthProvider {
  KAKAO,
  APPLE;

  public static OAuthProvider from(String name) {
    return switch (name.toLowerCase()) {
      case "kakao" -> KAKAO;
      case "apple" -> APPLE;
      default -> throw new IllegalArgumentException("Unknown OAuth provider: " + name);
    };
  }
}
