package com.grayzone.domain.company.service;

import com.grayzone.domain.company.dto.response.CompaniesSearchResponseDto;
import com.grayzone.domain.company.dto.response.CompaniesSuggestResponseDto;
import com.grayzone.domain.company.dto.response.CompanyDetailResponseDto;
import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.company.repository.projection.CompanySearchOnly;
import com.grayzone.domain.company.repository.projection.CompanySuggestionOnly;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewRatingRepository;
import com.grayzone.domain.review.repository.projection.CompanyTotalRatingOnly;
import com.grayzone.domain.review.repository.projection.ReviewTitleOnly;
import com.grayzone.domain.user.dto.response.UserFollowedCompaniesResponseDto;
import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.FollowCompanyRepository;
import com.grayzone.domain.user.repository.InterestedRegionRepository;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
  private final InterestedRegionRepository interestedRegionRepository;

  public CompanyDetailResponseDto getCompanyById(Long companyId, Long userId) {
    Double companyRating = Optional.ofNullable(reviewRatingRepository.getAverageScoreByCompanyId(companyId))
      .orElse(0.0);

    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new UpException(UpError.COMPANY_NOT_FOUND));

    boolean isFollowing = followCompanyRepository.existsByUserIdAndCompanyId(userId, companyId);

    return CompanyDetailResponseDto.from(company, companyRating, isFollowing);
  }

  public CompaniesSuggestResponseDto getSuggestedCompanies(
    String keyword,
    Double latitude,
    Double longitude,
    Pageable pageable
  ) {

    String booleanKeyword = generateBooleanKeyword(keyword);

    Slice<CompanySuggestionOnly> companies = companyRepository.suggestCompaniesByKeywordOrderByDistance(
      booleanKeyword,
      latitude,
      longitude,
      pageable
    );

    List<Long> companyIds = companies.getContent().stream().map(CompanySuggestionOnly::getId).toList();

    Map<Long, Double> totalRatings = getAverageRatings(companyIds);

    return CompaniesSuggestResponseDto.from(
      companies,
      totalRatings
    );
  }

  public CompaniesSearchResponseDto getSearchedCompaniesByKeyword(
    String keyword,
    Double latitude,
    Double longitude,
    User user,
    Pageable pageable
  ) {

    String booleanKeyword = generateBooleanKeyword(keyword);

    Page<CompanySearchOnly> companies = companyRepository.searchCompaniesByKeywordOrderByDistance(
      booleanKeyword,
      latitude,
      longitude,
      pageable
    );

    return buildCompaniesSearchResponseDto(companies, user);
  }

  public CompaniesSearchResponseDto getNearbyCompanies(
    Double latitude,
    Double longitude,
    User user,
    Pageable pageable
  ) {
    Page<CompanySearchOnly> companies = companyRepository.findCompaniesWithin3km(latitude, longitude, pageable);

    return buildCompaniesSearchResponseDto(companies, user);
  }

  public CompaniesSearchResponseDto getCompaniesByMainRegion(
    Double latitude,
    Double longitude,
    User user,
    Pageable pageable
  ) {
    Long mainRegionId = user.getMainRegion().getId();
    Page<CompanySearchOnly> companies = companyRepository.findCompaniesByRegion(
      latitude,
      longitude,
      List.of(mainRegionId),
      pageable
    );

    return buildCompaniesSearchResponseDto(companies, user);
  }

  public CompaniesSearchResponseDto getCompaniesByInterestedRegion(
    Double latitude,
    Double longitude,
    User user,
    Pageable pageable
  ) {

    List<InterestedRegion> interestedRegions = interestedRegionRepository.findAllByUserWithLegalDistrict(user);

    List<Long> interestedRegionIds = interestedRegions.stream()
      .map(InterestedRegion::getId)
      .toList();

    Page<CompanySearchOnly> companies = Page.empty(pageable);

    if (!interestedRegionIds.isEmpty()) {
      companies = companyRepository.findCompaniesByRegion(
        latitude,
        longitude,
        interestedRegionIds,
        pageable
      );
    }

    return buildCompaniesSearchResponseDto(companies, user);
  }

  public UserFollowedCompaniesResponseDto getCompaniesFollowedByUser(User user, Pageable pageable) {
    Slice<Company> followedCompanies = companyRepository.findFollowedCompaniesByUser(user, pageable);

    List<Long> followedCompanyIds = followedCompanies.getContent()
      .stream()
      .map(Company::getId)
      .toList();

    Map<Long, Double> totalRatings = getAverageRatings(followedCompanyIds);
    Map<Long, String> topReviews = getTopReviews(followedCompanyIds);

    return UserFollowedCompaniesResponseDto.from(followedCompanies, totalRatings, topReviews);
  }

  private String createRegionWildCard(LegalDistrict region) {
    return region.getAddress() + "%";
  }

  private String generateBooleanKeyword(String keyword) {
    return Arrays.stream(keyword.split(" "))
      .map(word -> "+" + word)
      .collect(Collectors.joining(" "));
  }

  private CompaniesSearchResponseDto buildCompaniesSearchResponseDto(
    Page<CompanySearchOnly> companies,
    User user
  ) {
    List<Long> companyIds = companies.getContent().stream()
      .map(CompanySearchOnly::getId)
      .toList();

    Map<Long, Double> totalRatings = getAverageRatings(companyIds);
    Set<Long> followedCompanyIds = getFollowedCompanyIds(user, companyIds);
    Map<Long, String> topReviews = getTopReviews(companyIds);

    return CompaniesSearchResponseDto.from(companies, totalRatings, followedCompanyIds, topReviews);
  }

  private Map<Long, Double> getAverageRatings(List<Long> companyIds) {
    return reviewRatingRepository.getAverageScoresByCompanyIds(companyIds)
      .stream()
      .collect(Collectors.toMap(
        CompanyTotalRatingOnly::getCompanyId,
        CompanyTotalRatingOnly::getTotalRating
      ));
  }

  private Set<Long> getFollowedCompanyIds(User user, List<Long> companyIds) {
    return new HashSet<>(followCompanyRepository
      .findFollowedCompanyIdsByUserIdAndCompanyIds(user.getId(), companyIds));
  }

  private Map<Long, String> getTopReviews(List<Long> companyIds) {
    return companyReviewRepository.findTopReviewPerCompany(companyIds)
      .stream()
      .collect(Collectors.toMap(
        ReviewTitleOnly::getCompanyId,
        ReviewTitleOnly::getTitle
      ));
  }
}
