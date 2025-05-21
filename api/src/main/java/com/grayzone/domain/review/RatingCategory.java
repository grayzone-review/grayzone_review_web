package com.grayzone.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RatingCategory {
  WORK_LIFE_BALANCE("워라밸"),
  SALARY("급여"),
  WELFARE("복지"),
  COMPANY_CULTURE("사내문화"),
  MANAGEMENT("경영진");

  private final String label;
}