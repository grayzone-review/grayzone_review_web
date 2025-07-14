package com.grayzone.domain.user.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {
  private final String nickname;
  private final String mainRegion;
  private final List<String> interestedRegions;

  public UserInfoResponseDto(String nickname, String mainRegion, List<String> interestedRegions) {
    this.nickname = nickname;
    this.mainRegion = mainRegion;
    this.interestedRegions = interestedRegions;
  }
}
