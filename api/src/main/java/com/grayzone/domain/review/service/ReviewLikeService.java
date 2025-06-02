package com.grayzone.domain.review.service;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewLike;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewLikeRepository;
import com.grayzone.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
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
      .orElseThrow(() -> new EntityNotFoundException("Company Review Not Found"));

    if (reviewLikeRepository.existsReviewLikesByUser((user))) {
      throw new IllegalArgumentException("Review Like Already Exists");
    }

    ReviewLike reviewLike = ReviewLike.builder()
      .companyReview(companyReview)
      .user(user)
      .build();

    reviewLikeRepository.save(reviewLike);
  }
}
