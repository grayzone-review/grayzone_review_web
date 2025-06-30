package com.grayzone.setup.legaldistrict;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LegalDistrictsApiResponse {
  private int page;
  private int perPage;
  private int totalCount;
  private int currentCount;
  private List<LegalDistrictApiResponse> data;

  @Getter
  @Setter
  static class LegalDistrictApiResponse {
    @JsonProperty("시도명")
    private String province;

    @JsonProperty("시군구명")
    private String city;

    @JsonProperty("읍면동명")
    private String town;

    @JsonProperty("리명")
    private String village;

    @JsonProperty("생성일자")
    private String createdDate;

    @JsonProperty("삭제일자")
    private String deletedDate;
  }
}