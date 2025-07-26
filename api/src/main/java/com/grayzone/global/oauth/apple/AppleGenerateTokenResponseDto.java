package com.grayzone.global.oauth.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleGenerateTokenResponseDto {
  @JsonProperty("refresh_token")
  private String refreshToken;
}
