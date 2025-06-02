package com.grayzone.domain.review.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.service.ReviewLikeService;
import com.grayzone.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewLikeController {

  private final ReviewLikeService reviewLikeService;

  @GetMapping("/{reviewId}/likes")
  public ResponseEntity<ResponseDataDto<Void>> createReviewLike(
    @PathVariable Long reviewId,
    @AuthenticationPrincipal User user
  ) {
    reviewLikeService.createReviewLike(reviewId, user);

    return ResponseEntity
      .status(HttpStatus.NO_CONTENT)
      .body(ResponseDataDto.from(null));
  }
}
