package com.grayzone.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDto {
  private String accessToken;
  private String refreshToken;
}
