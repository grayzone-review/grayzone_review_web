package com.grayzone.domain.review.service;

import com.grayzone.domain.review.dto.ReviewCommentListResponseDto;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommentService {

  private final ReviewCommentRepository reviewCommentRepository;
  private final CompanyReviewRepository companyReviewRepository;

  public ReviewCommentListResponseDto getCommentsByReviewId(
    Long reviewId,
    Long userId,
    Pageable pageable
  ) {

    if (!companyReviewRepository.existsById(reviewId)) {
      throw new EntityNotFoundException("Review not found");
    }

    return ReviewCommentListResponseDto.from(
      reviewCommentRepository.findByCompanyReviewId(reviewId, pageable),
      userId
    );
  }
}
