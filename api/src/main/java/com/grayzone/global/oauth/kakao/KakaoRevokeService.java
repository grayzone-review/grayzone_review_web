package com.grayzone.global.oauth.kakao;

import com.grayzone.domain.user.entity.User;
import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthRevokeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoRevokeService implements OAuthRevokeService {
  private final RestClient restClient = RestClient.create();

  @Value("${kakao.app-admin-key}")
  private String kakaoAdminKey;

  @Value("${kakao.revoke-uri}")
  private String kakaoRevokeUri;

  @Override
  public boolean support(OAuthProvider provider) {
    return provider == OAuthProvider.KAKAO;
  }

  @Override
  public void revoke(User user) {
    String oAuthId = user.getOAuthId();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("target_id_type", "user_id");
    body.add("target_id", oAuthId);

    restClient.post()
      .uri(kakaoRevokeUri)
      .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .body(body)
      .retrieve()
      .body(String.class);
  }
}
