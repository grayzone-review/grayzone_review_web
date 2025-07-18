package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewRating;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class CompanyReviewResponseDto {
  private Long id;
  private Map<String, Double> ratings;
  private String author;
  private String title;
  private String advantagePoint;
  private String disadvantagePoint;
  private String managementFeedback;
  private String jobRole;
  private String employmentPeriod;
  private LocalDateTime createdAt;
  private int likeCount;
  private boolean isLiked;
  private int commentCount;

  public static CompanyReviewResponseDto from(
    CompanyReview companyReview,
    boolean isLiked
  ) {
    Map<String, Double> ratings = companyReview.getRatings().stream()
      .collect(Collectors.toMap(
        ReviewRating::getCategoryName,
        ReviewRating::getRating
      ));

    return CompanyReviewResponseDto.builder()
      .id(companyReview.getId())
      .author(companyReview.getUser().getUsername())
      .title(companyReview.getTitle())
      .advantagePoint(companyReview.getAdvantagePoint())
      .disadvantagePoint(companyReview.getDisadvantagePoint())
      .managementFeedback(companyReview.getManagementFeedback())
      .jobRole(companyReview.getJobRole())
      .employmentPeriod(companyReview.getEmploymentPeriod())
      .createdAt(companyReview.getCreatedAt())
      .likeCount(companyReview.getLikeCount())
      .isLiked(isLiked)
      .ratings(ratings)
      .commentCount(companyReview.getCommentCount())
      .build();
  }
}
