package com.grayzone.global.oauth.kakao;

import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOAuthUserInfoProvider implements OAuthUserInfoProvider {
  private final RestClient restClient = RestClient.create();

  @Value("${kakao.user-info-uri}")
  private String kakaoUserInfoUri;

  @Override
  public boolean support(OAuthProvider provider) {
    return provider == OAuthProvider.KAKAO;
  }

  @Override
  public OAuthUserInfo parse(String token) {
    KakaoUserInfoResponse kakaoUserInfo = restClient.get()
      .uri(kakaoUserInfoUri)
      .header("Authorization", "Bearer " + token)
      .retrieve()
      .body(KakaoUserInfoResponse.class);

    if (!kakaoUserInfo.isEmailVerified() && !kakaoUserInfo.isEmailValid()) {
      throw new IllegalArgumentException("Invalid Token");
    }

    return new OAuthUserInfo(OAuthProvider.KAKAO, kakaoUserInfo.getEmail());
  }
}
