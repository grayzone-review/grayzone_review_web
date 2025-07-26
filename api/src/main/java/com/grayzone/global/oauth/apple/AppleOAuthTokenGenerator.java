package com.grayzone.global.oauth.apple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleOAuthTokenGenerator {
  private final RestClient restClient = RestClient.create();
  private final AppleUtils appleUtils;

  @Value("${apple.generate-token-uri}")
  private String appleGenerateTokenUri;

  @Value("${apple.client-id}")
  private String appleClientId;

  public AppleTokenResponse generateAppleToken(String code) {

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", appleClientId);
    body.add("client_secret", appleUtils.createClientSecret());
    body.add("code", code);

    AppleTokenResponse response = restClient.post()
      .uri(appleGenerateTokenUri)
      .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
      .body(body)
      .retrieve()
      .body(AppleTokenResponse.class);

    log.info(response.toString());

    return response;
  }

}
