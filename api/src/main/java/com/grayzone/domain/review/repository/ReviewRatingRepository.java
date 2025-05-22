package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.ReviewRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {
  @Query("""
    SELECT AVG(rr.rating)
    FROM ReviewRating rr
    WHERE rr.companyReview.company.id = :companyId
    """)
  public Double getAverageScoreByCompanyId(@Param("companyId") Long companyId);
}
