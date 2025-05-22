package com.grayzone.domain.company.service;

import com.grayzone.domain.company.dto.response.CompanyDetailResponseDto;
import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.review.repository.ReviewRatingRepository;
import com.grayzone.domain.user.repository.FollowCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final ReviewRatingRepository reviewRatingRepository;
  private final FollowCompanyRepository followCompanyRepository;

  public CompanyDetailResponseDto getCompanyById(Long companyId) {
    Double companyRating = Optional.ofNullable(reviewRatingRepository.getAverageScoreByCompanyId(companyId))
      .orElse(0.0);

    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new IllegalArgumentException("Company not found"));

    boolean isFollowing = followCompanyRepository.existsByUserIdAndCompanyId(1L, companyId);

    return CompanyDetailResponseDto.from(company, companyRating, isFollowing);
  }
}
