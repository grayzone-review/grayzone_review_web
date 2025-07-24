package com.grayzone.global.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponse {
  private Long id;
  @JsonProperty("kakao_account")
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
    @JsonProperty("is_email_valid")
    private boolean isEmailValid;
    @JsonProperty("is_email_verified")
    private boolean isEmailVerified;
    private String email;
  }
}
