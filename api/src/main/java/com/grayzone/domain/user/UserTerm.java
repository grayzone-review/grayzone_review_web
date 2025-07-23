package com.grayzone.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserTerm {
  SERVICE("serviceUse", "서비스 이용 약관", true, ""),
  PRIVACY("privacy", "개인정보 수집 및 이용 동의", true, ""),
  LOCATION("location", "위치기반 서비스 동의", true, "");

  private final String code;
  private final String title;
  private final boolean isRequired;
  private final String url;

  @JsonCreator
  public static UserTerm from(String code) {
    for (UserTerm term : UserTerm.values()) {
      if (term.getCode().equals(code)) {
        return term;
      }
    }
    throw new IllegalArgumentException("동의 항목을 확인해주세요: " + code);
  }
}
