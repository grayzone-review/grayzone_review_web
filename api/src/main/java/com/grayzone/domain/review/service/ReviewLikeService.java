package com.grayzone.domain.review.service;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewLike;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewLikeRepository;
import com.grayzone.domain.user.entity.User;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeService {

  private final ReviewLikeRepository reviewLikeRepository;
  private final CompanyReviewRepository companyReviewRepository;

  @Transactional
  public void createReviewLike(Long companyReviewId, User user) {
    CompanyReview companyReview = companyReviewRepository.findById(companyReviewId)
      .orElseThrow(() -> new UpException(UpError.REVIEW_NOT_FOUND));

    if (reviewLikeRepository.existsReviewLikesByCompanyReviewAndUser(companyReview, user)) {
      throw new UpException(UpError.REVIEW_ALREADY_LIKED);
    }

    ReviewLike reviewLike = ReviewLike.builder()
      .companyReview(companyReview)
      .user(user)
      .build();

    reviewLikeRepository.save(reviewLike);
  }

  @Transactional
  public void deleteReviewLike(Long companyReviewId, User user) {
    CompanyReview companyReview = companyReviewRepository.findById(companyReviewId)
      .orElseThrow(() -> new UpException(UpError.REVIEW_NOT_FOUND));

    ReviewLike reviewLike = reviewLikeRepository.findByCompanyReviewAndUser(companyReview, user)
      .orElseThrow(() -> new UpException(UpError.REVIEW_NOT_LIKED));

    reviewLikeRepository.delete(reviewLike);
  }
}
