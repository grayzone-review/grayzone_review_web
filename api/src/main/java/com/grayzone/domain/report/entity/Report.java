package com.grayzone.domain.report.entity;

import com.grayzone.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@NoArgsConstructor
public class Report extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String reporterName;

  @Column(nullable = false)
  private String targetName;

  @Column(nullable = false)
  private String reportType;

  @Column(nullable = false)
  private String description;

  public Report(
    String reporterName,
    String targetName,
    String reportType,
    String description
  ) {
    this.reporterName = reporterName;
    this.targetName = targetName;
    this.reportType = reportType;
    this.description = description;
  }
}
