package com.grayzone.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueRequestDto {
  @NotNull
  private String refreshToken;
}
