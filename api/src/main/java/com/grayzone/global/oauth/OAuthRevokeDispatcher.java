package com.grayzone.global.oauth;

import com.grayzone.domain.user.entity.User;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthRevokeDispatcher {
  private final List<OAuthRevokeService> revokeServices;

  public void dispatch(User user) {
    revokeServices.stream()
      .filter(revokeService -> revokeService.support(user.getOAuthProvider()))
      .findFirst()
      .orElseThrow(() -> new UpException(UpError.OAUTH_UNSUPPORTED_PROVIDER))
      .revoke(user);
  }
}
