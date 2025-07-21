package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.review.entity.CompanyReview;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class AggregatedCompanyReviewsResponseDto {
  List<AggregatedCompanyReviewResponseDto> reviews;
  private boolean hasNext;
  private int currentPage;

  public static AggregatedCompanyReviewsResponseDto from(
    Slice<CompanyReview> reviews,
    Set<Long> userLikedReviewIds,
    Map<Long, Double> totalRatings,
    Set<Long> followedCompanyIds,
    Map<Long, String> topReviews,
    Double latitude,
    Double longitude
  ) {

    List<AggregatedCompanyReviewResponseDto> dtos = reviews.getContent().stream()
      .map(companyReview -> {
          Company company = companyReview.getCompany();
          return AggregatedCompanyReviewResponseDto.from(
            companyReview,
            userLikedReviewIds.contains(companyReview.getId()),
            company,
            followedCompanyIds.contains(company.getId()),
            totalRatings.get(company.getId()),
            topReviews.get(company.getId()),
            latitude,
            longitude
          );
        }
      ).toList();

    return AggregatedCompanyReviewsResponseDto.builder()
      .reviews(dtos)
      .hasNext(reviews.hasNext())
      .currentPage(reviews.getNumber())
      .build();
  }

  @Getter
  @Builder
  static class AggregatedCompanyReviewResponseDto {
    private CompanyReviewResponseDto companyReview;
    private CompanyResponseDto company;

    public static AggregatedCompanyReviewResponseDto from(
      CompanyReview companyReview,
      boolean isLiked,
      Company company,
      boolean isFollowing,
      Double totalRating,
      String reviewTitle,
      Double latitude,
      Double longitude
    ) {
      return AggregatedCompanyReviewResponseDto.builder()
        .companyReview(CompanyReviewResponseDto.from(
          companyReview, isLiked
        ))
        .company(
          CompanyResponseDto.from(
            company, isFollowing, latitude, longitude, totalRating, reviewTitle
          )
        )
        .build();
    }

    @Getter
    @Builder
    public static class CompanyResponseDto {

      private Long id;
      private String companyName;
      private String companyAddress;
      private Double totalRating;
      private String reviewTitle;
      private boolean isFollowing;
      private Double distance;

      public static CompanyResponseDto from(
        Company company,
        boolean isFollowing,
        Double latitude,
        Double longitude,
        Double totalRating,
        String reviewTitle
      ) {
        return CompanyResponseDto.builder()
          .id(company.getId())
          .companyName(company.getBusinessName())
          .companyAddress(
            StringUtils.hasText(company.getSiteFullAddress())
              ? company.getSiteFullAddress()
              : company.getRoadNameAddress()
          )
          .reviewTitle(reviewTitle)
          .totalRating(totalRating)
          .isFollowing(isFollowing)
          .distance(company.calculateDistanceFrom(latitude, longitude))
          .build();
      }
    }
  }
}
