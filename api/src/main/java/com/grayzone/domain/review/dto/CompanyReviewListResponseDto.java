package com.grayzone.domain.review.dto;

import com.grayzone.domain.review.RatingCategory;
import com.grayzone.domain.review.entity.CompanyReview;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

@Getter
@Builder
public class CompanyReviewListResponseDto {

  private List<CompanyReviewResponseDto> reviews;
  private Boolean hasNext;

  public static CompanyReviewListResponseDto from(Page<CompanyReview> reviewPage) {

    List<CompanyReviewResponseDto> companyReviewResponseDtos = reviewPage.getContent().stream()
      .map(CompanyReviewResponseDto::from)
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
    private HashMap<RatingCategory, Double> ratings = new HashMap<>();
    private String title;
    private String advantagePoint;
    private String disadvantagePoint;
    private String managementFeedback;
    private String jobRole;
    private String employmentPeriod;
    private Integer likeCount;
    private Boolean isLiked;
    private Integer commentCount;

    public static CompanyReviewResponseDto from(
      CompanyReview companyReview
    ) {

      return CompanyReviewResponseDto.builder()
        .id(companyReview.getId())
        .title(companyReview.getTitle())
        .advantagePoint(companyReview.getAdvantagePoint())
        .disadvantagePoint(companyReview.getDisadvantagePoint())
        .managementFeedback(companyReview.getManagementFeedback())
        .jobRole(companyReview.getJobRole())
        .employmentPeriod(companyReview.getEmploymentPeriod())
        .likeCount(companyReview.getLikeCount())
        .commentCount(companyReview.getCommentCount())
        .build();
    }
  }
}
