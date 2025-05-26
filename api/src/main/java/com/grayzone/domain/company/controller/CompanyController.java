package com.grayzone.domain.company.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.company.dto.response.CompanyDetailResponseDto;
import com.grayzone.domain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping("/{companyId}")
  public ResponseEntity<ResponseDataDto<CompanyDetailResponseDto>> getCompany(
    @PathVariable Long companyId
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        companyService.getCompanyById(companyId))
    );
  }
}
