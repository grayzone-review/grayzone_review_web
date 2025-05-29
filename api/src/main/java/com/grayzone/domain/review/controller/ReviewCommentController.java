package com.grayzone.domain.review.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.dto.request.CreateReplyRequestDto;
import com.grayzone.domain.review.dto.request.CreateReviewCommentRequestDto;
import com.grayzone.domain.review.dto.response.CreateReplyResponseDto;
import com.grayzone.domain.review.dto.response.CreateReviewCommentResponseDto;
import com.grayzone.domain.review.dto.response.ReplyListResponseDto;
import com.grayzone.domain.review.dto.response.ReviewCommentListResponseDto;
import com.grayzone.domain.review.service.ReviewCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.getCommentsByReviewId(reviewId, 2L, pageable)
      )
    );
  }

  @GetMapping("/comments/{commentId}/replies")
  public ResponseEntity<ResponseDataDto<ReplyListResponseDto>> getReviewReplies(
    @PathVariable Long commentId,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.getReplyByParentId(commentId, 1L, pageable)
      )
    );
  }

  @PostMapping("/reviews/{reviewId}/comments")
  public ResponseEntity<ResponseDataDto<CreateReviewCommentResponseDto>> createReviewComment(
    @PathVariable Long reviewId,
    @Valid @RequestBody CreateReviewCommentRequestDto requestDto
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.createReviewComment(reviewId, 1L, requestDto)
      )
    );
  }

  @PostMapping("/comments/{commentId}/replies")
  public ResponseEntity<ResponseDataDto<CreateReplyResponseDto>> createReviewReplies(
    @PathVariable Long commentId,
    @Valid @RequestBody CreateReplyRequestDto requestDto
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        reviewCommentService.createReply(commentId, 2L, requestDto)
      )
    );
  }
}
