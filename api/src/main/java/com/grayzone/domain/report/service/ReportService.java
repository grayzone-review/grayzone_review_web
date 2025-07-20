package com.grayzone.domain.report.service;

import com.grayzone.domain.report.dto.ReportRequestDto;
import com.grayzone.domain.report.entity.Report;
import com.grayzone.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
  private final ReportRepository reportRepository;

  public void report(ReportRequestDto requestDto) {
    Report report = requestDto.toEntity();
    reportRepository.save(report);
  }
}
