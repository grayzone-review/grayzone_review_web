package com.grayzone.domain.review.entity;

import com.grayzone.domain.review.RatingCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_ratings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  @Builder
  public ReviewRating(
    RatingCategory category,
    Double rating,
    CompanyReview companyReview
  ) {
    this.category = category;
    this.rating = rating;
    this.companyReview = companyReview;
  }

  public String getCategoryName() {
    return category.getLabel();
  }
}
