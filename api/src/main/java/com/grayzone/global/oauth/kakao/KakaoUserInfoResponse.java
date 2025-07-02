package com.grayzone.global.oauth.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponse {
  private KakaoAccount kakaoAccount;

  public boolean isEmailValid() {
    return kakaoAccount != null && kakaoAccount.isEmailValid();
  }

  public boolean isEmailVerified() {
    return kakaoAccount != null && kakaoAccount.isEmailVerified();
  }

  public String getEmail() {
    return kakaoAccount.getEmail();
  }

  @Getter
  @Setter
  public static class KakaoAccount {
    private boolean isEmailValid;
    private boolean isEmailVerified;
    private String email;
  }
}
