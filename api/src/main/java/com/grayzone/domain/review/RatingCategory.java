package com.grayzone.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RatingCategory {
  WORK_LIFE_BALANCE("workLifeBalance"),
  SALARY("salary"),
  WELFARE("welfare"),
  COMPANY_CULTURE("companyCulture"),
  MANAGEMENT("management");

  private final String label;
}