package com.grayzone.domain.review.service;

import com.grayzone.domain.review.dto.request.CreateReplyRequestDto;
import com.grayzone.domain.review.dto.request.CreateReviewCommentRequestDto;
import com.grayzone.domain.review.dto.response.CreateReplyResponseDto;
import com.grayzone.domain.review.dto.response.CreateReviewCommentResponseDto;
import com.grayzone.domain.review.dto.response.ReplyListResponseDto;
import com.grayzone.domain.review.dto.response.ReviewCommentListResponseDto;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewComment;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReplyCountOnly;
import com.grayzone.domain.review.repository.ReviewCommentRepository;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.UserRepository;
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
  private final UserRepository userRepository;

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

  @Transactional
  public CreateReviewCommentResponseDto createReviewComment(
    Long reviewId,
    Long userId,
    CreateReviewCommentRequestDto requestDto
  ) {
    CompanyReview companyReview = companyReviewRepository.findById(reviewId)
      .orElseThrow(() -> new EntityNotFoundException("Review not found"));

    // TODO: 로그인 기능 구현 시 User 타입을 메서드 파라미터로 추가할 예정
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new EntityNotFoundException("로그인 기능 구현 시 User 타입을 메서드 파라미터로 추가할 예정"));

    ReviewComment comment = requestDto.toEntity(companyReview, user);

    return CreateReviewCommentResponseDto.from(reviewCommentRepository.save(comment));
  }

  @Transactional
  public CreateReplyResponseDto createReply(
    Long parentCommentId,
    Long userId,
    CreateReplyRequestDto requestDto
  ) {
    ReviewComment parentComment = reviewCommentRepository.findById(parentCommentId)
      .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

    if (!parentComment.canReply(userId)) {
      throw new EntityNotFoundException("답글을 작성할 수 없습니다.");
    }

    // TODO: 로그인 기능 구현 시 User 타입을 메서드 파라미터로 추가할 예정
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new EntityNotFoundException("로그인 기능 구현 시 User 타입을 메서드 파라미터로 추가할 예정"));

    ReviewComment comment = requestDto.toEntity(parentComment.getCompanyReview(), user, parentComment);

    return CreateReplyResponseDto.from(reviewCommentRepository.save(comment));
  }
}
