package com.grayzone.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum RatingCategory {
  WORK_LIFE_BALANCE("workLifeBalance"),
  SALARY("salary"),
  WELFARE("welfare"),
  COMPANY_CULTURE("companyCulture"),
  MANAGEMENT("management");

  private final String label;

  public static Optional<RatingCategory> fromLabel(String target) {
    return Arrays.stream(values())
      .filter(category -> category.label.equals(target))
      .findFirst();
  }
}