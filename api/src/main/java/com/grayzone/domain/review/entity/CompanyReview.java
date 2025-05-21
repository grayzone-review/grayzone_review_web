package com.grayzone.domain.review.entity;

import com.grayzone.common.BaseTimeEntity;
import com.grayzone.domain.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class CompanyReview extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String advantagePoint;
  @Column(nullable = false)
  private String disadvantagePoint;
  @Column(nullable = false)
  private String managementFeedback;
  @Column(nullable = false)
  private String jobRole;
  @Column(nullable = false)
  private String employmentPeriod;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "companyReview")
  private List<ReviewComment> comments = new ArrayList<>();
}
