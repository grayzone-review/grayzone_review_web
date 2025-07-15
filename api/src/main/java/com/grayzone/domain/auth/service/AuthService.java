package com.grayzone.domain.auth.service;

import com.grayzone.domain.auth.dto.request.LoginRequestDto;
import com.grayzone.domain.auth.dto.request.SignUpRequestDto;
import com.grayzone.domain.auth.dto.response.LoginResponseDto;
import com.grayzone.domain.auth.dto.response.ReissueResponseDto;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoDispatcher;
import com.grayzone.global.token.TokenManager;
import com.grayzone.global.token.TokenPair;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final InterestedRegionRepository interestedRegionRepository;
  private final OAuthUserInfoDispatcher oAuthUserInfoDispatcher;
  private final TokenManager tokenManager;

  @Transactional
  public void signUp(SignUpRequestDto requestDto) {
    if (userRepository.existsByNickname(requestDto.getNickname())) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }

    Long mainRegionId = requestDto.getMainRegionId();
    LegalDistrict mainRegion = legalDistrictRepository.findById(mainRegionId)
      .orElseThrow(() -> new IllegalArgumentException("메인 동네 지정을 필수입니다."));

//    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getOauthProvider(), requestDto.getOauthToken());
    OAuthUserInfo userInfo = new OAuthUserInfo(OAuthProvider.KAKAO, "test2123@test.com");

    User user = requestDto.toEntity(userInfo.getEmail(), mainRegion);
    List<InterestedRegion> interestedRegions = new ArrayList<>();

    if (!requestDto.getInterestedRegionIds().isEmpty()) {
      interestedRegions = legalDistrictRepository.findAllById(requestDto.getInterestedRegionIds())
        .stream()
        .map(legalDistrict -> new InterestedRegion(user, legalDistrict))
        .toList();
    }
    user.setInterestedRegions(interestedRegions);
    userRepository.save(user);
  }

  public LoginResponseDto login(LoginRequestDto requestDto) {
    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getOauthProvider(), requestDto.getOauthToken());

    User user = userRepository.findByEmail(userInfo.getEmail())
      .orElseThrow(() -> new EntityNotFoundException("비회원입니다."));

    TokenPair tokenPair = tokenManager.createTokenPair(user.getId());

    return LoginResponseDto.builder()
      .accessToken(tokenPair.getAccessToken())
      .refreshToken(tokenPair.getRefreshToken())
      .build();
  }

  public ReissueResponseDto reissue(String token) {
    if (!tokenManager.validateRefreshToken(token)) {
      throw new IllegalArgumentException("Invalid refresh token.");
    }

    long userId = tokenManager.parseUserIdFromRefreshToken(token);
    TokenPair tokenPair = tokenManager.createTokenPair(userId);

    tokenManager.invalidateRefreshToken(token);

    return ReissueResponseDto.builder()
      .accessToken(tokenPair.getAccessToken())
      .refreshToken(tokenPair.getRefreshToken())
      .build();
  }

}
