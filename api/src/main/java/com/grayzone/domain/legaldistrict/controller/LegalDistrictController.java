package com.grayzone.domain.legaldistrict.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.legaldistrict.dto.LegalDistrictsResponseDto;
import com.grayzone.domain.legaldistrict.service.LegalDistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/legal-districts")
@RequiredArgsConstructor
public class LegalDistrictController {
  private final LegalDistrictService legalDistrictService;

  @GetMapping
  public ResponseEntity<ResponseDataDto<LegalDistrictsResponseDto>> searchLegalDistricts(
    @RequestParam("keyword") String keyword,
    Pageable pageable
  ) {
    return ResponseEntity.ok(
      ResponseDataDto.from(
        legalDistrictService.getSearchedLegalDistricts(keyword, pageable)
      )
    );
  }
}
