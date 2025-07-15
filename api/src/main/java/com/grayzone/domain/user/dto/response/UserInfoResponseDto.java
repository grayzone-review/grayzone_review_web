package com.grayzone.domain.user.dto.response;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.user.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {
  private final String nickname;
  private final String mainRegionAddress;
  private final long mainRegionId;
  private final List<InterestedRegionResponseDto> interestedRegions;

  public UserInfoResponseDto(User user, List<LegalDistrict> interestedRegions) {
    LegalDistrict mainRegion = user.getMainRegion();

    this.nickname = user.getNickname();
    this.mainRegionAddress = mainRegion.getAddress();
    this.mainRegionId = mainRegion.getId();
    this.interestedRegions = interestedRegions.stream()
      .map(InterestedRegionResponseDto::from)
      .toList();
  }

  @Getter
  static class InterestedRegionResponseDto {
    private final long id;
    private final String address;

    InterestedRegionResponseDto(long interestedRegionId, String interestedRegionAddress) {
      this.id = interestedRegionId;
      this.address = interestedRegionAddress;
    }

    static InterestedRegionResponseDto from(LegalDistrict legalDistrict) {
      return new InterestedRegionResponseDto(
        legalDistrict.getId(),
        legalDistrict.getAddress()
      );
    }
  }
}
