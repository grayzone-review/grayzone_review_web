package com.grayzone.domain.user.service;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.dto.request.SignUpRequestDto;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final InterestedRegionRepository interestedRegionRepository;
  private final OAuthUserInfoDispatcher oAuthUserInfoDispatcher;

  public void verifyNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("Nickname already exists");
    }
  }

  public void signUp(SignUpRequestDto requestDto) {
    Long mainRegionId = requestDto.getMainRegionId();
    LegalDistrict mainRegion = legalDistrictRepository.findById(mainRegionId)
      .orElseThrow(() -> new IllegalArgumentException("메인 동네 지정을 필수입니다."));

    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getOAuthProvider(), requestDto.getToken());

    User user = requestDto.toEntity(userInfo.getEmail(), mainRegion);
    userRepository.save(user);

    List<InterestedRegion> interestedRegions = legalDistrictRepository.findAllById(requestDto.getInterestedRegionIds())
      .stream().map(legalDistrict -> new InterestedRegion(user, legalDistrict)).toList();

    interestedRegionRepository.saveAll(interestedRegions);
  }
}
