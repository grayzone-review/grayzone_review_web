package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.review.entity.CompanyReview;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Getter
@Builder
public class CompanyReviewsResponseDto {

  private List<CompanyReviewResponseDto> reviews;
  private boolean hasNext;
  private int currentPage;

  public static CompanyReviewsResponseDto from(Page<CompanyReview> reviewPage, Set<Long> userLikedReviewIds) {

    List<CompanyReviewResponseDto> companyReviewResponseDtos = reviewPage.getContent().stream()
      .map(
        (element) -> CompanyReviewResponseDto.from(
          element,
          userLikedReviewIds.contains(element.getId())
        )
      )
      .toList();

    return CompanyReviewsResponseDto
      .builder()
      .reviews(companyReviewResponseDtos)
      .hasNext(reviewPage.hasNext())
      .currentPage(reviewPage.getNumber())
      .build();
  }
}
