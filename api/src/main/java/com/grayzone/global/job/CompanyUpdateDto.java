package com.grayzone.global.job;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyUpdateDto {
  private Long id;
  private Long legalDistrictId;

  public CompanyUpdateDto(Long id, Long legalDistrictId) {
    this.id = id;
    this.legalDistrictId = legalDistrictId;
  }
}
