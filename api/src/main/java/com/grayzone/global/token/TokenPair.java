package com.grayzone.global.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenPair {
  private final String accessToken;
  private final String refreshToken;
}
