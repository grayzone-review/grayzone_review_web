package com.grayzone.domain.company.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.company.dto.response.CompaniesSearchResponseDto;
import com.grayzone.domain.company.dto.response.CompaniesSuggestResponseDto;
import com.grayzone.domain.company.dto.response.CompanyDetailResponseDto;
import com.grayzone.domain.company.service.CompanyService;
import com.grayzone.domain.user.entity.User;
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
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping("/{companyId}")
  public ResponseEntity<ResponseDataDto<CompanyDetailResponseDto>> getCompany(
    @PathVariable Long companyId,
    @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyService.getCompanyById(companyId, user.getId()))
    );
  }

  @GetMapping("/search")
  public ResponseEntity<ResponseDataDto<CompaniesSearchResponseDto>> searchCompanies(
    @RequestParam("keyword") String keyword,
    @RequestParam("latitude") Double latitude,
    @RequestParam("longitude") Double longitude,
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {

    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyService.getSearchedCompaniesByKeyword(keyword, latitude, longitude, user, pageable)
      )
    );
  }

  @GetMapping("/suggestions")
  public ResponseEntity<ResponseDataDto<CompaniesSuggestResponseDto>> suggestCompanies(
    @RequestParam("keyword") String keyword,
    @RequestParam("latitude") Double latitude,
    @RequestParam("longitude") Double longitude,
    @AuthenticationPrincipal User user,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyService.getSuggestedCompanies(keyword, latitude, longitude, pageable)
      )
    );
  }
}
