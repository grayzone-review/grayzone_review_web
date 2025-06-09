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
    SELECT AVG(rr.rating)
    FROM ReviewRating rr
    WHERE rr.companyReview.company.id IN :companyIds
    GROUP BY rr.companyReview.company.id
    """)
  List<CompanyTotalRatingOnly> getCompaniesAverageScoreByCompanyIds(@Param("companyIds") List<Long> companyIds);
}
