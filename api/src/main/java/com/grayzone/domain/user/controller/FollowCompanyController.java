package com.grayzone.domain.user.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.service.FollowCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class FollowCompanyController {

  private final FollowCompanyService followCompanyService;

  @PostMapping("/{companyId}/follows")
  public ResponseEntity<ResponseDataDto<Void>> createFollowCompany(
    @PathVariable Long companyId,
    @AuthenticationPrincipal User user
  ) {
    followCompanyService.createFollowCompany(companyId, user);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
