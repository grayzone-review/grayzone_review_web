package com.grayzone.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequestDto {
  private String refreshToken;
}
