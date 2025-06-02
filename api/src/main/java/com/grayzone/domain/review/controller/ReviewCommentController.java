package com.grayzone.domain.review.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.dto.request.CreateReplyRequestDto;
import com.grayzone.domain.review.dto.request.CreateReviewCommentRequestDto;
import com.grayzone.domain.review.dto.response.CreateReplyResponseDto;
import com.grayzone.domain.review.dto.response.CreateReviewCommentResponseDto;
import com.grayzone.domain.review.dto.response.ReplyListResponseDto;
import com.grayzone.domain.review.dto.response.ReviewCommentListResponseDto;
import com.grayzone.domain.review.service.ReviewCommentService;
import com.grayzone.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewCommentController {

  private final ReviewCommentService reviewCommentService;

  @GetMapping("/reviews/{reviewId}/comments")
  public ResponseEntity<ResponseDataDto<ReviewCommentListResponseDto>> getReviewComments(
    @PathVariable Long reviewId,
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.getCommentsByReviewId(reviewId, user.getId(), pageable)
      )
    );
  }

  @GetMapping("/comments/{commentId}/replies")
  public ResponseEntity<ResponseDataDto<ReplyListResponseDto>> getReviewReplies(
    @PathVariable Long commentId,
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.getReplyByParentId(commentId, user.getId(), pageable)
      )
    );
  }

  @PostMapping("/reviews/{reviewId}/comments")
  public ResponseEntity<ResponseDataDto<CreateReviewCommentResponseDto>> createReviewComment(
    @PathVariable Long reviewId,
    @Valid @RequestBody CreateReviewCommentRequestDto requestDto,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.createReviewComment(reviewId, user.getId(), requestDto)
      )
    );
  }

  @PostMapping("/comments/{commentId}/replies")
  public ResponseEntity<ResponseDataDto<CreateReplyResponseDto>> createReviewReplies(
    @PathVariable Long commentId,
    @Valid @RequestBody CreateReplyRequestDto requestDto,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.createReply(commentId, user.getId(), requestDto)
      )
    );
  }
}
