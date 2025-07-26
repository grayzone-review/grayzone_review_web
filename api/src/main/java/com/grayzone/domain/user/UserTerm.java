package com.grayzone.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserTerm {
  SERVICE(
    "serviceUse",
    "서비스 이용 약관",
    true,
    "https://translucent-fall-6f6.notion.site/23c10746acf98094aa7ee08bf627d7f8?source=copy_link"
  ),
  PRIVACY(
    "privacy",
    "개인정보 수집 및 이용 동의",
    true,
    "https://translucent-fall-6f6.notion.site/23c10746acf980248e6ef9e7665dd3d3?source=copy_link"
  ),
  LOCATION(
    "location",
    "위치기반 서비스 동의",
    true,
    "https://translucent-fall-6f6.notion.site/23c10746acf9804fa5cec48c5f26b371?source=copy_link"
  );

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
    throw new UpException(UpError.INVALID_REQUEST);
  }
}
