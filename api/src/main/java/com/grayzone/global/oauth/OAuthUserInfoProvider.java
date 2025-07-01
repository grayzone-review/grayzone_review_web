package com.grayzone.global.oauth;

public interface OAuthUserInfoProvider {

  boolean support(OAuthProvider provider);

  OAuthUserInfo parse(String token);
}
