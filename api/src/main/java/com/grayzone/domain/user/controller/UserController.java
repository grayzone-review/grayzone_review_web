package com.grayzone.domain.user.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.service.CompanyReviewService;
import com.grayzone.domain.user.dto.request.VerifyNicknameDuplicateRequestDto;
import com.grayzone.domain.user.dto.response.UserInfoResponseDto;
import com.grayzone.domain.user.dto.response.UserRelatedReviewsResponseDto;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final CompanyReviewService companyReviewService;

  @PostMapping("/nickname-verify")
  public ResponseEntity<ResponseDataDto<Void>> verifyNicknameDuplicate(
    @Valid @RequestBody VerifyNicknameDuplicateRequestDto requestDto
  ) {
    userService.verifyNicknameDuplicate(requestDto.getNickname());

    return ResponseEntity
      .status(HttpStatus.OK)
      .body(new ResponseDataDto<>(null, "사용가능한 닉네임입니다."));
  }

  @GetMapping("/me")
  public ResponseEntity<ResponseDataDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(
      ResponseDataDto.from(userService.getUserInfo(user))
    );
  }

  @GetMapping("/me/reviews")
  public ResponseEntity<ResponseDataDto<UserRelatedReviewsResponseDto>> getMyReviews(
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyReviewService.getReviewsByUser(user, pageable)
      )
    );
  }
}
