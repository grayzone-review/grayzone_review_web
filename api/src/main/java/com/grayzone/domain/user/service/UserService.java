package com.grayzone.domain.user.service;

import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.grayzone.domain.user.dto.request.WithdrawRequestDto;
import com.grayzone.domain.user.dto.response.UserInfoResponseDto;
import com.grayzone.domain.user.dto.response.UserInteractionCountsResponseDto;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.domain.user.repository.UserRepository;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import com.grayzone.global.oauth.OAuthRevokeDispatcher;
import com.grayzone.global.token.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final InterestedRegionRepository interestedRegionRepository;
  private final CompanyReviewRepository companyReviewRepository;
  private final CompanyRepository companyRepository;
  private final LegalDistrictRepository legalDistrictRepository;
  private final TokenManager tokenManager;
  private final OAuthRevokeDispatcher oAuthRevokeDispatcher;

  public void verifyNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new UpException(UpError.NICKNAME_DUPLICATE);
    }
  }

  public UserInfoResponseDto getUserInfo(User user) {
    List<LegalDistrict> interestedRegions = interestedRegionRepository.findAllByUserWithLegalDistrict(user).stream()
      .map(InterestedRegion::getLegalDistrict)
      .toList();

    return new UserInfoResponseDto(user, interestedRegions);
  }

  public UserInteractionCountsResponseDto getUserInteractionCounts(User user) {
    return UserInteractionCountsResponseDto.builder()
      .myReviewCount(companyReviewRepository.countByUser(user))
      .likeOrCommentReviewCount(companyReviewRepository.countByUserInteracted(user))
      .followCompanyCount(companyRepository.countFollowedCompaniesByUser(user))
      .build();
  }

  @Transactional
  public void updateUserInfo(User user, UpdateUserInfoRequestDto requestDto) {
    User updatedUser = userRepository.findById(user.getId())
      .orElseThrow(() -> new UpException(UpError.USER_NOT_FOUND));

    boolean isNicknameChanged = requestDto.getNickname().equals(updatedUser.getNickname());
    if (!isNicknameChanged && userRepository.existsByNickname(requestDto.getNickname())) {
      throw new UpException(UpError.NICKNAME_DUPLICATE);
    }

    updatedUser.setNickname(requestDto.getNickname());

    if (!updatedUser.getMainRegion().getId().equals(requestDto.getMainRegionId())) {
      LegalDistrict mainRegion = legalDistrictRepository.findById(requestDto.getMainRegionId())
        .orElseThrow(() -> new UpException(UpError.REGION_NOT_FOUND));
      updatedUser.setMainRegion(mainRegion);
    }

    List<InterestedRegion> interestedRegions = legalDistrictRepository.findAllById(requestDto.getInterestedRegionIds())
      .stream()
      .map(legalDistrict -> new InterestedRegion(updatedUser, legalDistrict))
      .toList();

    updatedUser.setInterestedRegions(interestedRegions);
    userRepository.save(updatedUser);
  }

  @Transactional
  public void withdraw(User user, WithdrawRequestDto requestDto) {
    User deletedUser = userRepository.findById(user.getId())
      .orElseThrow(() -> new UpException(UpError.USER_NOT_FOUND));

    try {
      tokenManager.invalidateRefreshToken(requestDto.getRefreshToken());
    } catch (Exception e) {
      log.warn("Refresh token invalidation failed", e);
    }

    try {
      oAuthRevokeDispatcher.dispatch(user);
    } catch (Exception e) {
      log.warn("OAuth revoke dispatch failed", e);
    }

    deletedUser.inactive();
    userRepository.save(deletedUser);
  }
}
