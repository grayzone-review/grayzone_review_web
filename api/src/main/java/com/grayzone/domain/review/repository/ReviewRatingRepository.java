package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.ReviewRating;
import com.grayzone.domain.review.repository.projection.CompanyTotalRatingOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {
  @Query("""
    SELECT AVG(rr.rating)
    FROM ReviewRating rr
    WHERE rr.companyReview.company.id = :companyId
    """)
  Double getAverageScoreByCompanyId(@Param("companyId") Long companyId);

  @Query("""
    SELECT c.id AS companyId, COALESCE(AVG(r.rating), 0) AS totalRating
    FROM Company c
    LEFT JOIN c.companyReviews cr
    LEFT JOIN cr.ratings r
    WHERE c.id IN :companyIds
    GROUP BY c.id
    """)
  List<CompanyTotalRatingOnly> getAverageScoresByCompanyIds(@Param("companyIds") List<Long> companyIds);
}
