package com.grayzone.domain.company.dto.response;

import com.grayzone.domain.company.repository.projection.CompanySearchOnly;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class CompaniesSuggestResponseDto {

  private List<CompanyResponseDto> companies;
  private boolean hasNext;
  private int currentPage;

  @Getter
  @Builder
  public static class CompanyResponseDto {

    private Long id;
    private String companyName;
    private String companyAddress;
    private Double totalRating;

    public static CompaniesSearchResponseDto.CompanyResponseDto from(
      CompanySearchOnly company,
      Double totalRating
    ) {
      return CompaniesSearchResponseDto.CompanyResponseDto.builder()
        .id(company.getId())
        .companyName(company.getCompanyName())
        .companyAddress(
          Objects.requireNonNullElse(
            company.getSiteFullAddress(),
            company.getRoadNameAddress()
          )
        )
        .totalRating(totalRating)
        .build();
    }
  }
}
