package com.grayzone.domain.company.service;

import com.grayzone.domain.company.dto.response.CompaniesSearchResponseDto;
import com.grayzone.domain.company.dto.response.CompanyDetailResponseDto;
import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.company.repository.projection.CompanySearchOnly;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewRatingRepository;
import com.grayzone.domain.review.repository.projection.CompanyTotalRatingOnly;
import com.grayzone.domain.review.repository.projection.ReviewTitleOnly;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.FollowCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final ReviewRatingRepository reviewRatingRepository;
  private final FollowCompanyRepository followCompanyRepository;
  private final CompanyReviewRepository companyReviewRepository;

  public CompanyDetailResponseDto getCompanyById(Long companyId, Long userId) {
    Double companyRating = Optional.ofNullable(reviewRatingRepository.getAverageScoreByCompanyId(companyId))
      .orElse(0.0);

    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new IllegalArgumentException("Company not found"));

    boolean isFollowing = followCompanyRepository.existsByUserIdAndCompanyId(userId, companyId);

    return CompanyDetailResponseDto.from(company, companyRating, isFollowing);
  }

  public CompaniesSearchResponseDto getCompaniesByKeyword(
    String keyword,
    Double latitude,
    Double longitude,
    User user,
    Pageable pageable
  ) {
    Page<CompanySearchOnly> companies = companyRepository.findByKeywordOrderByDistance(
      keyword,
      latitude,
      longitude,
      pageable
    );

    List<Long> companyIds = companies.getContent().stream().map(CompanySearchOnly::getId).toList();

    List<CompanyTotalRatingOnly> averageScoresByCompanyIds = reviewRatingRepository.getAverageScoresByCompanyIds(companyIds);
    Map<Long, Double> totalRatings = averageScoresByCompanyIds
      .stream()
      .collect(
        Collectors.toMap(
          CompanyTotalRatingOnly::getCompanyId,
          CompanyTotalRatingOnly::getTotalRating
        )
      );

    Set<Long> followedCompanyIds = new HashSet<>(followCompanyRepository
      .findFollowedCompanyIdsByUserIdAndCompanyIds(user.getId(), companyIds));

    Map<Long, String> topReviews = companyReviewRepository
      .findTopReviewPerCompany(companyIds)
      .stream()
      .collect(Collectors.toMap(
        ReviewTitleOnly::getCompanyId,
        ReviewTitleOnly::getTitle
      ));

    return CompaniesSearchResponseDto.from(companies, totalRatings, followedCompanyIds, topReviews);
  }
}
