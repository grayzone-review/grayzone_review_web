package com.grayzone.domain.report.controller;

import com.grayzone.common.ResponseDataDto;
import com.grayzone.domain.report.dto.ReportRequestDto;
import com.grayzone.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

  private final ReportService reportService;

  @PostMapping
  public ResponseEntity<ResponseDataDto<Void>> report(
    @Valid @RequestBody ReportRequestDto reportRequestDto
  ) {
    reportService.report(reportRequestDto);
    return ResponseEntity.ok(
      ResponseDataDto.from(
        null
      )
    );
  }
}
