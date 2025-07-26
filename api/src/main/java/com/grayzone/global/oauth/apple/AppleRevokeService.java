package com.grayzone.global.oauth.apple;

import com.grayzone.domain.user.entity.User;
import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthRevokeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleRevokeService implements OAuthRevokeService {

  private final RestClient restClient = RestClient.create();
  private final AppleUtils appleUtils;

  @Value("${apple.revoke-token-uri}")
  private String appleRevokeTokenUri;

  @Value("${apple.client-id}")
  private String appleClientId;

  @Override
  public boolean support(OAuthProvider provider) {
    return provider == OAuthProvider.APPLE;
  }

  @Override
  public void revoke(User user) {
    String clientSecret = appleUtils.createClientSecret();
    String refreshToken = user.getOAuthRefreshToken();

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("client_id", appleClientId);
    body.add("client_secret", clientSecret);
    body.add("token", refreshToken);
    body.add("token_type_hint", "refresh_token");

    ResponseEntity<Void> bodilessEntity = restClient.post()
      .uri(appleRevokeTokenUri)
      .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .body(body)
      .retrieve()
      .toBodilessEntity();
  }
}
