package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.repository.projection.ReviewTitleOnly;
import com.grayzone.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    WHERE cr.createdAt >= :sinceDate
    GROUP BY cr.id
    ORDER BY COUNT(l) DESC, cr.createdAt DESC
    """
  )
  Slice<CompanyReview> findCompanyReviewsOrderByLikeCountDesc(@Param("sinceDate") LocalDate sinceDate, Pageable pageable);

  @EntityGraph(attributePaths = {"company"})
  @Query(value = """
    SELECT cr FROM CompanyReview cr
    LEFT JOIN cr.company c
    WHERE c.legalDistrict.id = :mainRegionId
    ORDER BY cr.createdAt DESC
    """
  )
  Slice<CompanyReview> findCompanyReviewsByMainRegion(
    Pageable pageable,
    @Param("mainRegionId") Long mainRegionId
  );

  @Query("""
    SELECT cr FROM CompanyReview cr
    LEFT JOIN cr.company c
    WHERE c.legalDistrict.id IN (:interestedRegionIds)
    ORDER BY cr.createdAt DESC
    """)
  Slice<CompanyReview> findCompanyReviewByInterestedRegions(
    Pageable pageable,
    @Param("interestedRegionIds") List<Long> interestedRegionIds
  );

  Slice<CompanyReview> findByUser(User user, Pageable pageable);

  @Query("""
        SELECT DISTINCT r
        FROM CompanyReview r
        LEFT JOIN r.likes l
        LEFT JOIN r.comments c
        WHERE l.user = :user OR c.user = :user
    """)
  Page<CompanyReview> findDistinctByUserInteracted(@Param("user") User user, Pageable pageable);

  long countByUser(User user);

  @Query("""
        SELECT COUNT(DISTINCT r.id)
        FROM CompanyReview r
        LEFT JOIN r.likes l
        LEFT JOIN r.comments c
        WHERE l.user = :user OR c.user = :user
    """)
  long countByUserInteracted(@Param("user") User user);
}
