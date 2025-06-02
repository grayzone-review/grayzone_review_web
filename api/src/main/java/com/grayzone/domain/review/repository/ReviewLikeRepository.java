package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewLike;
import com.grayzone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
  @Query("""
    SELECT l.companyReview.id
    FROM ReviewLike l
    WHERE l.user.id = :userId AND l.companyReview.id IN :reviewIds
    """)
  Set<Long> findReviewIdsLikedByUser(
    @Param("userId") Long userId,
    @Param("reviewIds") List<Long> reviewIds
  );

  Optional<ReviewLike> findByCompanyReviewAndUser(CompanyReview companyReview, User user);

  boolean existsReviewLikesByCompanyReviewAndUser(CompanyReview companyReview, User user);
}
