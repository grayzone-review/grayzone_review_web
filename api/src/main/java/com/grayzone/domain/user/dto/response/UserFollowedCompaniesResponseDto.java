package com.grayzone.domain.user.dto.response;

import com.grayzone.domain.company.entity.Company;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class UserFollowedCompaniesResponseDto {
  private List<UserFollowedCompanyDto> companies;
  private boolean hasNext;
  private int currentPage;

  public static UserFollowedCompaniesResponseDto from(
    Slice<Company> companies,
    Map<Long, Double> totalRatings,
    Map<Long, String> topReviews
  ) {

    List<UserFollowedCompanyDto> userFollowedCompanyDtos = companies.getContent()
      .stream()
      .map(company -> UserFollowedCompanyDto.from(
        company,
        totalRatings.get(company.getId()),
        topReviews.get(company.getId())
      ))
      .toList();

    return UserFollowedCompaniesResponseDto.builder()
      .companies(userFollowedCompanyDtos)
      .hasNext(companies.hasNext())
      .currentPage(companies.getNumber())
      .build();
  }


  @Getter
  @Builder
  public static class UserFollowedCompanyDto {

    private Long id;
    private String companyName;
    private String companyAddress;
    private Double totalRating;
    private String reviewTitle;

    public static UserFollowedCompanyDto from(Company company, Double totalRating, String reviewTitle) {
      return UserFollowedCompanyDto.builder()
        .id(company.getId())
        .companyName(company.getBusinessName())
        .companyAddress(
          StringUtils.hasText(company.getSiteFullAddress())
            ? company.getSiteFullAddress()
            : company.getRoadNameAddress()
        )
        .totalRating(totalRating)
        .reviewTitle(reviewTitle)
        .build();
    }
  }
}
