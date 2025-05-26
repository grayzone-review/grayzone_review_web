package com.grayzone.domain.review.entity;

import com.grayzone.common.BaseTimeEntity;
import com.grayzone.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewComment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_review_id")
  private CompanyReview companyReview;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private ReviewComment parent;

  @Column(nullable = false)
  private boolean isSecret;

  public String getAuthorName() {
    return user.getUsername();
  }

  public boolean isVisibility(Long userId) {
    if (!isSecret) {
      return true;
    }
    Long reviewId = this.companyReview.getId();

    return reviewId.equals(userId) || id.equals(userId);
  }
}
