package com.grayzone.domain.review.dto.request;

import lombok.Setter;

@Setter
public class CreateCompanyReviewRequestDto {
  private String advantagePoint;
  private String disadvantagePoint;
  private String managementFeedback;
  private String jobRole;
  private String employmentPeriod;
  private RatingRequestDto ratings;

  @Setter
  static class RatingRequestDto {
    private Double workLifeBalance;
    private Double salary;
    private Double welfare;
    private Double companyCulture;
    private Double management;
  }
}
