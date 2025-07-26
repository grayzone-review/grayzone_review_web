package com.grayzone.domain.auth.service;

import com.grayzone.domain.auth.dto.request.SignUpRequestDto;
import com.grayzone.domain.auth.dto.response.LoginResponseDto;
import com.grayzone.domain.auth.dto.response.ReissueResponseDto;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import com.grayzone.global.oauth.OAuthProvider;
import com.grayzone.global.oauth.OAuthUserInfo;
import com.grayzone.global.oauth.OAuthUserInfoDispatcher;
import com.grayzone.global.oauth.apple.AppleOAuthTokenGenerator;
import com.grayzone.global.oauth.apple.AppleTokenResponse;
import com.grayzone.global.token.TokenManager;
import com.grayzone.global.token.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final OAuthUserInfoDispatcher oAuthUserInfoDispatcher;
  private final TokenManager tokenManager;
  private final AppleOAuthTokenGenerator oAuthTokenGenerator;

  @Transactional
  public void signUp(SignUpRequestDto requestDto) {
    if (userRepository.existsByNickname(requestDto.getNickname())) {
      throw new UpException(UpError.NICKNAME_DUPLICATE);
    }

    Long mainRegionId = requestDto.getMainRegionId();
    LegalDistrict mainRegion = legalDistrictRepository.findById(mainRegionId)
      .orElseThrow(() -> new UpException(UpError.REGION_NOT_FOUND));

    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(requestDto.getOauthProvider(), requestDto.getOauthToken());

    User user = requestDto.toEntity(userInfo, mainRegion);
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

  @Transactional
  public LoginResponseDto login(Map<String, String> requestMap) {
    String oAuthToken = requestMap.get("oauthToken");
    String provider = requestMap.get("provider");
    String authorizationCode = requestMap.get("authorizationCode");

    if (oAuthToken == null || provider == null) {
      throw new UpException(UpError.INVALID_REQUEST);
    }

    OAuthProvider oAuthProvider = OAuthProvider.from(provider);

    if (oAuthProvider == OAuthProvider.APPLE && authorizationCode == null) {
      throw new UpException(UpError.INVALID_REQUEST);
    }

    OAuthUserInfo userInfo = oAuthUserInfoDispatcher.dispatch(oAuthProvider, oAuthToken);

    User user = userRepository.findByoAuthId(userInfo.getOAuthId())
      .orElseThrow(() -> new UpException(UpError.UNAUTHORIZED_USER));

    if (user.requiresAppleRefreshToken()) {
      AppleTokenResponse appleToken = oAuthTokenGenerator.generateAppleToken(authorizationCode);
      user.setOAuthRefreshToken(appleToken.getRefreshToken());
      userRepository.save(user);
    }

    TokenPair tokenPair = tokenManager.createTokenPair(user.getId());

    return LoginResponseDto.builder()
      .accessToken(tokenPair.getAccessToken())
      .refreshToken(tokenPair.getRefreshToken())
      .build();
  }

  public void logout(String refreshToken) {
    tokenManager.invalidateRefreshToken(refreshToken);
  }

  public ReissueResponseDto reissue(String token) {
    if (!tokenManager.validateRefreshToken(token)) {
      throw new UpException(UpError.REFRESH_TOKEN_INVALID);
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
