package com.grayzone.domain.auth.service;

import com.grayzone.domain.auth.dto.request.LoginRequestDto;
import com.grayzone.domain.auth.dto.request.SignUpRequestDto;
import com.grayzone.domain.auth.dto.response.LoginResponseDto;
import com.grayzone.domain.auth.dto.response.SignUpResponseDto;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoDispatcher;
import com.grayzone.global.token.TokenManager;
import com.grayzone.global.token.TokenPair;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
    if (userRepository.existsByNickname(requestDto.getNickname())) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }

    Long mainRegionId = requestDto.getMainRegionId();
    LegalDistrict mainRegion = legalDistrictRepository.findById(mainRegionId)
      .orElseThrow(() -> new IllegalArgumentException("메인 동네 지정을 필수입니다."));

    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getOAuthProvider(), requestDto.getToken());

    User user = requestDto.toEntity(userInfo.getEmail(), mainRegion);
    userRepository.save(user);

    if (!requestDto.getInterestedRegionIds().isEmpty()) {
      List<InterestedRegion> interestedRegions = legalDistrictRepository.findAllById(requestDto.getInterestedRegionIds())
        .stream()
        .map(legalDistrict -> new InterestedRegion(user, legalDistrict))
        .toList();

      interestedRegionRepository.saveAll(interestedRegions);
    }

    TokenPair tokenPair = tokenManager.createTokenPair(user);

    return SignUpResponseDto.builder()
      .accessToken(tokenPair.getAccessToken())
      .refreshToken(tokenPair.getRefreshToken())
      .build();
  }

  public LoginResponseDto login(LoginRequestDto requestDto) {
    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getProvider(), requestDto.getOauthToken());

    User user = userRepository.findByEmail(userInfo.getEmail())
      .orElseThrow(() -> new EntityNotFoundException("비회원입니다."));

    TokenPair tokenPair = tokenManager.createTokenPair(user);

    return LoginResponseDto.builder()
      .accessToken(tokenPair.getAccessToken())
      .refreshToken(tokenPair.getRefreshToken())
      .build();
  }


}
