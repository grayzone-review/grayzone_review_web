package com.grayzone.domain.user.dto.request;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.user.UserTerm;
import com.grayzone.domain.user.entity.User;
import com.grayzone.global.oauth.OAuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class SignUpRequestDto {
  @NotBlank
  private String token;
  @NotBlank
  private OAuthProvider oAuthProvider;
  @NotNull
  private Long mainRegionId;

  @NotNull
  @Size(max = 3)
  private List<Long> interestedRegionIds;

  @Length(min = 2, max = 12, message = "2~12자 이내로 입력가능하며, 한글, 영문, 숫자 사용이 가능합니다.")
  @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "한글, 영문, 숫자만 입력 가능하며 공백은 허용되지 않습니다.")
  private String nickname;

  private List<UserTerm> agreements;

  public User toEntity(String email, LegalDistrict mainRegion) {
    validateAgreements(agreements);

    return User.builder()
      .email(email)
      .nickname(nickname)
      .oAuthProvider(oAuthProvider)
      .mainRegion(mainRegion)
      .agreedServiceUse(true)
      .agreedPrivacy(true)
      .agreedLocation(true)
      .build();
  }

  private void validateAgreements(List<UserTerm> agreements) {
    long requiredCount = Stream.of(UserTerm.values())
      .filter(UserTerm::isRequired)
      .count();

    if (agreements.size() < requiredCount) {
      throw new IllegalArgumentException("필수 항목을 동의해주세요.");
    }
  }
}
