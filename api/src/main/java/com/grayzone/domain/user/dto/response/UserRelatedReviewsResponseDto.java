package com.grayzone.domain.user.dto.response;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewRating;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserRelatedReviewsResponseDto {

  private List<UserRelatedReviewResponseDto> reviews;

  public static UserRelatedReviewsResponseDto from(Slice<CompanyReview> companyReviews) {
    List<UserRelatedReviewResponseDto> userRelatedReviewResponseDtos = companyReviews.getContent().stream()
      .map(UserRelatedReviewResponseDto::from)
      .toList();

    return UserRelatedReviewsResponseDto.builder()
      .reviews(userRelatedReviewResponseDtos)
      .build();
  }

  @Getter
  @Builder
  public static class UserRelatedReviewResponseDto {
    private Long id;
    private Double totalRating;
    private String title;
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String jobRole;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;

    public static UserRelatedReviewResponseDto from(CompanyReview companyReview) {
      Double totalRating = companyReview.getRatings().stream()
        .mapToDouble(ReviewRating::getRating)
        .average()
        .orElse(0.0);

      Company company = companyReview.getCompany();

      return UserRelatedReviewResponseDto.builder()
        .id(companyReview.getId())
        .title(companyReview.getTitle())
        .totalRating(totalRating)
        .companyId(company.getId())
        .companyName(company.getBusinessName())
        .companyAddress(
          StringUtils.hasText(company.getSiteFullAddress())
            ? company.getSiteFullAddress()
            : company.getRoadNameAddress()
        )
        .jobRole(companyReview.getJobRole())
        .createdAt(companyReview.getCreatedAt())
        .likeCount(companyReview.getLikeCount())
        .commentCount(companyReview.getCommentCount())
        .build();
    }
  }
}
