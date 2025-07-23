package com.grayzone.domain.review.service;

import com.grayzone.domain.review.dto.request.CreateReplyRequestDto;
import com.grayzone.domain.review.dto.request.CreateReviewCommentRequestDto;
import com.grayzone.domain.review.dto.response.CreateReplyResponseDto;
import com.grayzone.domain.review.dto.response.CreateReviewCommentResponseDto;
import com.grayzone.domain.review.dto.response.RepliesResponseDto;
import com.grayzone.domain.review.dto.response.ReviewCommentsResponseDto;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewComment;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewCommentRepository;
import com.grayzone.domain.review.repository.projection.ReplyCountOnly;
import com.grayzone.domain.user.entity.User;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
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

  public ReviewCommentsResponseDto getCommentsByReviewId(
    Long reviewId,
    Long userId,
    Pageable pageable
  ) {

    if (!companyReviewRepository.existsById(reviewId)) {
      throw new UpException(UpError.REVIEW_NOT_FOUND);
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

    return ReviewCommentsResponseDto.from(
      commentsPage,
      userId,
      replyCounts
    );
  }

  public RepliesResponseDto getReplyByParentId(
    Long parentId,
    Long userId,
    Pageable pageable
  ) {
    if (!reviewCommentRepository.existsById(parentId)) {
      throw new UpException(UpError.REVIEW_NOT_FOUND);
    }

    Page<ReviewComment> replies = reviewCommentRepository.findByParentId(parentId, pageable);

    return RepliesResponseDto.from(replies, userId);
  }

  @Transactional
  public CreateReviewCommentResponseDto createReviewComment(
    Long reviewId,
    User user,
    CreateReviewCommentRequestDto requestDto
  ) {
    CompanyReview companyReview = companyReviewRepository.findById(reviewId)
      .orElseThrow(() -> new UpException(UpError.REVIEW_NOT_FOUND));

    ReviewComment comment = requestDto.toEntity(companyReview, user);

    return CreateReviewCommentResponseDto.from(reviewCommentRepository.save(comment));
  }

  @Transactional
  public CreateReplyResponseDto createReply(
    Long parentCommentId,
    User user,
    CreateReplyRequestDto requestDto
  ) {
    ReviewComment parentComment = reviewCommentRepository.findById(parentCommentId)
      .orElseThrow(() -> new UpException(UpError.COMMENT_NOT_FOUND));

    if (!parentComment.canReply(user.getId())) {
      throw new UpException(UpError.COMMENT_NO_PERMISSION);
    }

    ReviewComment comment = requestDto
      .toEntity(parentComment.getCompanyReview(), user, parentComment);

    return CreateReplyResponseDto.from(reviewCommentRepository.save(comment));
  }
}
