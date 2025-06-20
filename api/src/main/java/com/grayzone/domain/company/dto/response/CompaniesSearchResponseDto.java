package com.grayzone.domain.company.dto.response;

import com.grayzone.domain.company.repository.projection.CompanySearchOnly;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class CompaniesSearchResponseDto {

  private List<CompanyResponseDto> companies;
  private long totalCount;
  private boolean hasNext;
  private int currentPage;

  public static CompaniesSearchResponseDto from(
    Page<CompanySearchOnly> companies,
    Map<Long, Double> totalRatings,
    Set<Long> followedCompanyIds,
    Map<Long, String> topReviews
  ) {

    List<CompanyResponseDto> companyDtos = companies.getContent().stream()
      .map(
        (company) -> CompanyResponseDto.from(
          company,
          totalRatings.get(company.getId()),
          followedCompanyIds.contains(company.getId()),
          topReviews.get(company.getId())
        )
      ).toList();

    return CompaniesSearchResponseDto.builder()
      .companies(companyDtos)
      .totalCount(companies.getTotalElements())
      .hasNext(companies.hasNext())
      .currentPage(companies.getNumber())
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
      CompanySearchOnly company,
      Double totalRating,
      boolean isFollowing,
      String reviewTitle
    ) {
      return CompanyResponseDto.builder()
        .id(company.getId())
        .companyName(company.getCompanyName())
        .companyAddress(
          StringUtils.hasText(company.getSiteFullAddress())
            ? company.getSiteFullAddress()
            : company.getRoadNameAddress()
        )
        .reviewTitle(reviewTitle)
        .totalRating(totalRating)
        .isFollowing(isFollowing)
        .distance(company.getDistance())
        .build();
    }
  }
}
