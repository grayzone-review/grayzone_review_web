package com.grayzone.domain.review.service;

import com.grayzone.domain.review.dto.ReplyListResponseDto;
import com.grayzone.domain.review.dto.ReviewCommentListResponseDto;
import com.grayzone.domain.review.entity.ReviewComment;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReplyCountOnly;
import com.grayzone.domain.review.repository.ReviewCommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

    Page<ReviewComment> commentsPage = reviewCommentRepository.findByCompanyReviewIdAndParentIsNull(
      reviewId,
      pageable
    );

    Map<Long, Integer> replyCounts = reviewCommentRepository.countRepliesByParentIds(
      commentsPage.getContent().stream()
        .map(ReviewComment::getId)
        .toList()
    ).stream().collect(Collectors.toMap(ReplyCountOnly::getReviewId, ReplyCountOnly::getCount));

    return ReviewCommentListResponseDto.from(
      commentsPage,
      userId,
      replyCounts
    );
  }

  public ReplyListResponseDto getReplyByParentId(
    Long parentId,
    Long userId,
    Pageable pageable
  ) {
    if (!reviewCommentRepository.existsById(parentId)) {
      throw new EntityNotFoundException("Review not found");
    }

    Page<ReviewComment> replies = reviewCommentRepository.findByParentId(parentId, pageable);

    return ReplyListResponseDto.from(replies, userId);
  }
}
