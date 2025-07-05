package com.grayzone.domain.auth.dto.request;

import com.grayzone.global.oauth.OAuthProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
  @NotNull
  private String oauthToken;

  @NotNull
  private OAuthProvider oauthProvider;
}
