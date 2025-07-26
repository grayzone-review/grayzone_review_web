package com.grayzone.global.oauth;

import com.grayzone.domain.user.entity.User;

public interface OAuthRevokeService {
  boolean support(OAuthProvider provider);

  void revoke(User user);
}
