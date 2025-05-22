package com.grayzone.domain.company.dto.response;

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
  private Double xCoordinate;
  private Double yCoordinate;
  private Double totalRating;
  private Boolean isFollowing;
}
