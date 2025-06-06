package com.grayzone.domain.company.dto.response;

import com.grayzone.domain.company.entity.Company;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CompanyDetailResponseDto {

  private Long id;
  private String companyName;
  private LocalDateTime permissionDate;
  private String siteFullAddress;
  private String roadNameAddress;
  private Double latitude;
  private Double longitude;
  private Double totalRating;
  private boolean isFollowing;

  public static CompanyDetailResponseDto from(Company company, Double totalRating, boolean isFollowing) {
    return CompanyDetailResponseDto.builder()
      .id(company.getId())
      .companyName(company.getBusinessName())
      .permissionDate(company.getPermissionDate())
      .siteFullAddress(company.getSiteFullAddress())
      .roadNameAddress(company.getRoadNameAddress())
      .latitude(company.getLatitude())
      .longitude(company.getLongitude())
      .totalRating(totalRating)
      .isFollowing(isFollowing)
      .build();
  }
}
