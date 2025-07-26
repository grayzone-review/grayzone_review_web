package com.grayzone.global.oauth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleTokenResponse {
  @JsonProperty("refresh_token")
  private String refreshToken;
}
