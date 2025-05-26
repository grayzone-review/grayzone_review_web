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


  @Column(nullable = false)
  private boolean isSecret;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private ReviewComment parent;

  public String getAuthorName() {
    return user.getUsername();
  }

  private boolean isAuthor(Long userId) {
    return getUser().getId().equals(userId);
  }

  private boolean isReviewAuthor(Long userId) {
    return companyReview.getAuthorId().equals(userId);
  }

  public boolean isVisibleTo(Long userId) {
    if (!isSecret) {
      return true;
    }

    boolean isReviewAuthor = isReviewAuthor(userId);
    boolean isCommentAuthor = isAuthor(userId);
    boolean isParentAuthor = parent != null && parent.isAuthor(userId);

    return isReviewAuthor || isCommentAuthor || isParentAuthor;
  }
}
