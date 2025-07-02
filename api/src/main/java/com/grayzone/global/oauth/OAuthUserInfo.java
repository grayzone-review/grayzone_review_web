package com.grayzone.global.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserInfo {

  private OAuthProvider provider;
  private String email;
}
