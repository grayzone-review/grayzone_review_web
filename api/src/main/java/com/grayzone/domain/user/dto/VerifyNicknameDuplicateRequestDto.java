package com.grayzone.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class VerifyNicknameDuplicateRequestDto {
  @Length(min = 2, max = 12, message = "2~12자 이내로 입력가능하며, 한글, 영문, 숫자 사용이 가능합니다.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "한글, 영문, 숫자만 입력 가능하며 공백은 허용되지 않습니다.")
  private String nickname;
}
