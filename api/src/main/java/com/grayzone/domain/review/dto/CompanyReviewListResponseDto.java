package com.grayzone.domain.review.dto;

import com.grayzone.domain.review.RatingCategory;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewRating;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class CompanyReviewListResponseDto {

  private List<CompanyReviewResponseDto> reviews;
  private Boolean hasNext;

  public static CompanyReviewListResponseDto from(Page<CompanyReview> reviewPage, Set<Long> userLikedReviewIds) {

    List<CompanyReviewResponseDto> companyReviewResponseDtos = reviewPage.getContent().stream()
      .map(
        (element) -> CompanyReviewResponseDto.from(
          element,
          userLikedReviewIds.contains(element.getId())
        )
      )
      .toList();

    return CompanyReviewListResponseDto
      .builder()
      .reviews(companyReviewResponseDtos)
      .hasNext(reviewPage.hasNext())
      .build();
  }

  @Getter
  @Builder
  public static class CompanyReviewResponseDto {
    private Long id;
    private Map<RatingCategory, Double> ratings;
    private String title;
    private String advantagePoint;
    private String disadvantagePoint;
    private String managementFeedback;
    private String jobRole;
    private String employmentPeriod;
    private int likeCount;
    private boolean isLiked;
    private int commentCount;

    public static CompanyReviewResponseDto from(
      CompanyReview companyReview,
      boolean isLiked
    ) {

      Map<RatingCategory, Double> ratings = companyReview.getRatings().stream()
        .collect(Collectors.toMap(
          ReviewRating::getCategory,
          ReviewRating::getRating
        ));

      return CompanyReviewResponseDto.builder()
        .id(companyReview.getId())
        .title(companyReview.getTitle())
        .advantagePoint(companyReview.getAdvantagePoint())
        .disadvantagePoint(companyReview.getDisadvantagePoint())
        .managementFeedback(companyReview.getManagementFeedback())
        .jobRole(companyReview.getJobRole())
        .employmentPeriod(companyReview.getEmploymentPeriod())
        .likeCount(companyReview.getLikeCount())
        .isLiked(isLiked)
        .ratings(ratings)
        .commentCount(companyReview.getCommentCount())
        .build();
    }
  }
}
