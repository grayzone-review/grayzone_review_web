package com.grayzone.domain.report.dto;

import com.grayzone.domain.report.entity.Report;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Setter
public class ReportRequestDto {

  @NotBlank
  private String reporterName;
  
  private String targetName;

  @NotBlank
  private String reportType;

  @NotBlank
  private String description;

  public Report toEntity() {
    return new Report(
      this.reporterName,
      this.targetName,
      this.reportType,
      this.description
    );
  }
}
