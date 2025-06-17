package com.grayzone.domain.review.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.dto.request.CreateCompanyReviewRequestDto;
import com.grayzone.domain.review.dto.response.CompanyReviewResponseDto;
import com.grayzone.domain.review.dto.response.CompanyReviewsResponseDto;
import com.grayzone.domain.review.service.CompanyReviewService;
import com.grayzone.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyReviewController {

  private final CompanyReviewService companyReviewService;

  @GetMapping("/{companyId}/reviews")
  public ResponseEntity<ResponseDataDto<CompanyReviewsResponseDto>> getCompanyReviews(
    @PathVariable Long companyId,
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyReviewService.getReviewsByCompanyId(companyId, user.getId(), pageable)
      )
    );
  }

  @PostMapping("/{companyId}/reviews")
  public ResponseEntity<ResponseDataDto<CompanyReviewResponseDto>> createCompanyReview(
    @PathVariable Long companyId,
    @Valid @RequestBody CreateCompanyReviewRequestDto requestDto,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyReviewService.createCompanyReview(companyId, requestDto, user)
      )
    );
  }
}
