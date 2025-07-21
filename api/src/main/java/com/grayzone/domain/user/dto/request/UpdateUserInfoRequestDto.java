package com.grayzone.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
public class UpdateUserInfoRequestDto {
  @NotNull
  private Long mainRegionId;

  @NotNull
  @Size(max = 3)
  private List<Long> interestedRegionIds;

  @Length(min = 2, max = 12, message = "2~12자 이내로 입력가능하며, 한글, 영문, 숫자 사용이 가능합니다.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "한글, 영문, 숫자만 입력 가능하며 공백은 허용되지 않습니다.")
  private String nickname;
}
