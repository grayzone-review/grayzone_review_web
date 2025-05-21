package com.grayzone.domain.review.entity;

import com.grayzone.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewComment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String contents;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_review_id")
  private CompanyReview companyReview;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private ReviewComment parent;

  @Column(nullable = false)
  private boolean isSecret;
}
