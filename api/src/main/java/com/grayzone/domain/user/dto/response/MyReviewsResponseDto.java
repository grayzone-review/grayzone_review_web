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
public class MyReviewsResponseDto {

  private List<MyReviewResponseDto> reviews;

  public static MyReviewsResponseDto from(Slice<CompanyReview> companyReviews) {
    List<MyReviewResponseDto> myReviewResponseDtos = companyReviews.getContent().stream()
      .map(MyReviewResponseDto::from)
      .toList();

    return MyReviewsResponseDto.builder()
      .reviews(myReviewResponseDtos)
      .build();
  }

  @Getter
  @Builder
  public static class MyReviewResponseDto {
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

    public static MyReviewResponseDto from(CompanyReview companyReview) {
      Double totalRating = companyReview.getRatings().stream()
        .mapToDouble(ReviewRating::getRating)
        .average()
        .orElse(0.0);

      Company company = companyReview.getCompany();

      return MyReviewResponseDto.builder()
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
