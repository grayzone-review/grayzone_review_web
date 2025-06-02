package com.grayzone.domain.review.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.review.dto.response.CompanyReviewListResponseDto;
import com.grayzone.domain.review.service.CompanyReviewService;
import com.grayzone.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyReviewController {

  private final CompanyReviewService companyReviewService;

  @GetMapping("/{companyId}/reviews")
  public ResponseEntity<ResponseDataDto<CompanyReviewListResponseDto>> getCompanyReviews(
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
}
