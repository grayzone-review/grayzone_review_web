package com.grayzone.global.oauth;

import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
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
      .orElseThrow(() -> new UpException(UpError.OAUTH_UNSUPPORTED_PROVIDER))
      .parse(token);
  }
}
