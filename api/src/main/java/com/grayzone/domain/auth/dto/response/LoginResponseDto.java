package com.grayzone.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
  private String accessToken;
  private String refreshToken;
}
