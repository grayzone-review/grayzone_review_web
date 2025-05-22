package com.grayzone.domain.review.entity;

import com.grayzone.domain.review.RatingCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "review_ratings")
public class ReviewRating {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private RatingCategory category;

  @Column(nullable = false)
  private Double rating;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_review_id")
  private CompanyReview companyReview;
}
