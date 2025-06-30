package com.grayzone.domain.legaldistrict.dto;

import com.grayzone.domain.legaldistrict.repository.projection.LegalDistrictPrefixOnly;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class LegalDistrictsResponseDto {
  private long totalCount;
  private boolean hasNext;
  private int currentPage;
  private List<LegalDistrictResponseDto> legalDistricts;

  public static LegalDistrictsResponseDto from(Page<LegalDistrictPrefixOnly> legalDistricts) {
    List<LegalDistrictResponseDto> legalDistrictResponseDtos = legalDistricts.getContent().stream().map(LegalDistrictResponseDto::from)
      .toList();
    return LegalDistrictsResponseDto.builder()
      .totalCount(legalDistricts.getTotalElements())
      .hasNext(legalDistricts.hasNext())
      .currentPage(legalDistricts.getNumber())
      .legalDistricts(legalDistrictResponseDtos)
      .build();
  }

  @Getter
  @Builder
  public static class LegalDistrictResponseDto {
    private long id;
    private String name;

    public static LegalDistrictResponseDto from(LegalDistrictPrefixOnly legalDistrict) {
      return LegalDistrictResponseDto.builder()
        .id(legalDistrict.getId())
        .name(legalDistrict.getLegalDistrict())
        .build();
    }
  }
}
