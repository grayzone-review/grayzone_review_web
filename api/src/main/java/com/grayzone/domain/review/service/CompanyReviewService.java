package com.grayzone.domain.review.service;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.review.ReviewTitleSummarizer;
import com.grayzone.domain.review.dto.request.CreateCompanyReviewRequestDto;
import com.grayzone.domain.review.dto.response.AggregatedCompanyReviewsResponseDto;
import com.grayzone.domain.review.dto.response.CompanyReviewResponseDto;
import com.grayzone.domain.review.dto.response.CompanyReviewsResponseDto;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewRating;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewLikeRepository;
import com.grayzone.domain.review.repository.ReviewRatingRepository;
import com.grayzone.domain.review.repository.projection.CompanyTotalRatingOnly;
import com.grayzone.domain.review.repository.projection.ReviewTitleOnly;
import com.grayzone.domain.user.dto.response.UserRelatedReviewsResponseDto;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyReviewService {

  private final CompanyReviewRepository companyReviewRepository;
  private final CompanyRepository companyRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final ReviewRatingRepository reviewRatingRepository;
  private final FollowCompanyRepository followCompanyRepository;
  private final ReviewTitleSummarizer reviewTitleSummarizer;
  private final InterestedRegionRepository interestedRegionRepository;


  public CompanyReviewsResponseDto getReviewsByCompanyId(Long companyId, Long userId, Pageable pageable) {
    if (!companyRepository.existsById(companyId)) {
      throw new UpException(UpError.COMPANY_NOT_FOUND);
    }

    Page<CompanyReview> reviewPage = companyReviewRepository.findByCompanyId(companyId, pageable);

    Set<Long> userLikedReviewIds = getUserLikedReviewIds(reviewPage.getContent(), userId);
    return CompanyReviewsResponseDto.from(
      reviewPage,
      userLikedReviewIds
    );
  }

  public AggregatedCompanyReviewsResponseDto getPopularCompanyReviews(
    Pageable pageable,
    Double latitude,
    Double longitude,
    User user
  ) {
    Slice<CompanyReview> popularCompanyReviews = companyReviewRepository
      .findCompanyReviewsOrderByLikeCountDesc(pageable);

    return buildAggregatedCompanyReviews(popularCompanyReviews, latitude, longitude, user);
  }

  public AggregatedCompanyReviewsResponseDto getMainRegionLatestCompanyReviews(
    Pageable pageable,
    Double latitude,
    Double longitude,
    User user
  ) {
    String mainRegionAddress = addWildCardSuffix(user.getMainRegionAddress());

    Slice<CompanyReview> mainRegionLatestCompanyReviews = companyReviewRepository
      .findCompanyReviewsByMainRegion(pageable, mainRegionAddress);

    return buildAggregatedCompanyReviews(mainRegionLatestCompanyReviews, latitude, longitude, user);
  }

  public AggregatedCompanyReviewsResponseDto getInterestedRegionLatestCompanyReviews(
    Pageable pageable,
    Double latitude,
    Double longitude,
    User user
  ) {
    List<InterestedRegion> interestedRegions = interestedRegionRepository.findAllByUserWithLegalDistrict(user);

    List<String> interestedRegionAddresses = interestedRegions.stream()
      .map(InterestedRegion::getLegalDistrict)
      .map(LegalDistrict::getAddress)
      .map(this::addWildCardSuffix)
      .toList();

    String address1 = !interestedRegionAddresses.isEmpty() ? interestedRegionAddresses.get(0) : null;
    String address2 = interestedRegionAddresses.size() > 1 ? interestedRegionAddresses.get(1) : null;
    String address3 = interestedRegionAddresses.size() > 2 ? interestedRegionAddresses.get(2) : null;


    Slice<CompanyReview> interestedRegionsLatestCompanyReviews = companyReviewRepository
      .findCompanyReviewByInterestedRegions(pageable, address1, address2, address3);

    return buildAggregatedCompanyReviews(interestedRegionsLatestCompanyReviews, latitude, longitude, user);
  }

  public UserRelatedReviewsResponseDto getReviewsByUser(User user, Pageable pageable) {
    return UserRelatedReviewsResponseDto.from(
      companyReviewRepository.findByUser(user, pageable)
    );
  }

  public UserRelatedReviewsResponseDto getInteractedReviews(User user, Pageable pageable) {
    return UserRelatedReviewsResponseDto.from(
      companyReviewRepository.findDistinctByUserInteracted(user, pageable)
    );
  }

  @Transactional
  public CompanyReviewResponseDto createCompanyReview(
    Long companyId,
    CreateCompanyReviewRequestDto requestDto,
    User user
  ) {
    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new UpException(UpError.COMPANY_NOT_FOUND));

    String title = reviewTitleSummarizer.summarize(requestDto.getSummarizeSourceText());

    CompanyReview companyReview = requestDto.toCompanyReview(company, user, title);
    companyReviewRepository.save(companyReview);

    List<ReviewRating> reviewRatings = requestDto.toReviewRatings(companyReview);
    reviewRatingRepository.saveAll(reviewRatings);
    companyReview.setRatings(reviewRatings);

    return CompanyReviewResponseDto.from(companyReview, false);
  }

  private AggregatedCompanyReviewsResponseDto buildAggregatedCompanyReviews(
    Slice<CompanyReview> companyReviews,
    Double latitude,
    Double longitude,
    User user
  ) {
    Set<Long> userLikedReviewIds = getUserLikedReviewIds(companyReviews.getContent(), user.getId());

    List<Long> companyIds = companyReviews.getContent()
      .stream()
      .map(element -> element.getCompany().getId())
      .toList();

    Map<Long, Double> averageRatings = getAverageRatings(companyIds);
    Set<Long> followedCompanyIds = getFollowedCompanyIds(user, companyIds);
    Map<Long, String> topReviews = getTopReviews(companyIds);

    return AggregatedCompanyReviewsResponseDto.from(
      companyReviews,
      userLikedReviewIds,
      averageRatings,
      followedCompanyIds,
      topReviews,
      latitude,
      longitude
    );
  }

  private Set<Long> getUserLikedReviewIds(List<CompanyReview> reviews, Long userId) {
    List<Long> reviewIds = reviews
      .stream()
      .map(CompanyReview::getId)
      .toList();

    return reviewIds.isEmpty()
      ? Set.of()
      : reviewLikeRepository.findReviewIdsLikedByUser(userId, reviewIds);
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

  private String addWildCardSuffix(String target) {
    return target + "%";
  }
}
