package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.repository.projection.ReviewTitleOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {
  Page<CompanyReview> findByCompanyId(Long companyId, Pageable pageable);

  @Query(value = """
    SELECT cr.company_id AS companyId,
           cr.title AS title
    FROM (
        SELECT cr.company_id,
               cr.title,
               ROW_NUMBER() OVER (
                   PARTITION BY cr.company_id
                   ORDER BY COUNT(rl.id) DESC, cr.created_at DESC
               ) AS rn
        FROM company_reviews cr
        LEFT JOIN review_likes rl ON rl.company_review_id = cr.id
        WHERE cr.company_id IN (:companyIds)
        GROUP BY cr.id
    ) cr
    WHERE cr.rn = 1
    """, nativeQuery = true)
  List<ReviewTitleOnly> findTopReviewPerCompany(@Param("companyIds") List<Long> companyIds);

  @Query(value = """
    SELECT cr FROM CompanyReview cr
    LEFT JOIN cr.likes l
    GROUP BY cr.id
    ORDER BY COUNT(l) DESC, cr.createdAt DESC
    """
  )
  Slice<CompanyReview> findCompanyReviewsOrderByLikeCountDesc(Pageable pageable);
}
