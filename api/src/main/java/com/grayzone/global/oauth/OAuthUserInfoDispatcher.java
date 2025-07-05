package com.grayzone.global.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthUserInfoDispatcher {
  private final List<OAuthUserInfoProvider> oAuthUserInfoProviders;

  public OAuthUserInfo dispatch(OAuthProvider provider, String token) {
    return oAuthUserInfoProviders.stream()
      .filter(oAuthUserInfoProvider -> oAuthUserInfoProvider.support(provider))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Not Support Provider"))
      .parse(token);
  }
}
