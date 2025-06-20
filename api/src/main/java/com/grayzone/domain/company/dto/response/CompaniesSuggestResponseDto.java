package com.grayzone.domain.company.dto.response;

import com.grayzone.domain.company.repository.projection.CompanySuggestionOnly;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class CompaniesSuggestResponseDto {

  private List<CompanyResponseDto> companies;
  private boolean hasNext;
  private int currentPage;

  public static CompaniesSuggestResponseDto from(
    Slice<CompanySuggestionOnly> companies,
    Map<Long, Double> totalRatings
  ) {
    List<CompanyResponseDto> companyDtos = companies.getContent()
      .stream()
      .map((company) -> CompanyResponseDto.from(
        company,
        totalRatings.get(company.getId())
      )).toList();

    return CompaniesSuggestResponseDto.builder()
      .companies(companyDtos)
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

    public static CompanyResponseDto from(
      CompanySuggestionOnly company,
      Double totalRating
    ) {
      return CompanyResponseDto.builder()
        .id(company.getId())
        .companyName(company.getCompanyName())
        .companyAddress(
          StringUtils.hasText(company.getSiteFullAddress())
            ? company.getSiteFullAddress()
            : company.getRoadNameAddress()
        )
        .totalRating(totalRating)
        .build();
    }
  }
}
