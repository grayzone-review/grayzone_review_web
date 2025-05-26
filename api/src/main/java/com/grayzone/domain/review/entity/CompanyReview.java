package com.grayzone.domain.review.entity;

import com.grayzone.common.BaseTimeEntity;
import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "companyReview", fetch = FetchType.LAZY)
  private List<ReviewComment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "companyReview", fetch = FetchType.LAZY)
  private List<ReviewLike> likes = new ArrayList<>();

  @OneToMany(mappedBy = "companyReview", fetch = FetchType.LAZY)
  private List<ReviewRating> ratings = new ArrayList<>();

  public int getLikeCount() {
    return likes.size();
  }

  public int getCommentCount() {
    return comments.size();
  }

  public Long getAuthorId() {
    return user.getId();
  }
}
