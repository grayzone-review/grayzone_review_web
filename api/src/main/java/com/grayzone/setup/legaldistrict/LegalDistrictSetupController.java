package com.grayzone.setup.legaldistrict;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LegalDistrictSetupController {
  private final LegalDistrictSetupService legalDistrictSetupService;

  @GetMapping("legal-districts/setup")
  public void setupLegalDistricts() {
    legalDistrictSetupService.setupLegalDistricts();
  }
}
