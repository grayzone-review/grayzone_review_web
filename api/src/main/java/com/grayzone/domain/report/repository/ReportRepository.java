package com.grayzone.domain.report.repository;

import com.grayzone.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
  
}
