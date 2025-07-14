package com.grayzone.domain.user.service;

import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.user.dto.response.UserInfoResponseDto;
import com.grayzone.domain.user.dto.response.UserInteractionCountsResponseDto;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.domain.user.repository.UserRepository;
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

  public void verifyNicknameDuplicate(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("중복된 닉네임입니다.");
    }
  }

  public UserInfoResponseDto getUserInfo(User user) {
    List<String> interestedRegionAddresses = interestedRegionRepository.findAllByUserWithLegalDistrict(user).stream()
      .map(interestedRegion -> interestedRegion.getLegalDistrict().getAddress())
      .toList();

    return new UserInfoResponseDto(user.getNickname(), user.getMainRegionAddress(), interestedRegionAddresses);
  }

  public UserInteractionCountsResponseDto getUserInteractionCounts(User user) {
    return UserInteractionCountsResponseDto.builder()
      .myReviewCount(companyReviewRepository.countByUser(user))
      .likeOrCommentReviewCount(companyReviewRepository.countByUserInteracted(user))
      .followCompanyCount(companyRepository.countFollowedCompaniesByUser(user))
      .build();
  }
}
