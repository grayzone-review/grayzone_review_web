package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.ReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

  Page<ReviewComment> findByCompanyReviewIdAndParentIsNull(Long reviewId, Pageable pageable);

  @Query("""
    SELECT rc.id AS reviewId, COALESCE(COUNT(rcr), 0) AS count
    FROM ReviewComment rc
    LEFT JOIN ReviewComment rcr ON rcr.parent.id = rc.id
    WHERE rc.id in :parentIds
    GROUP BY rc.id
    """)
  List<ReplyCountOnly> countRepliesByParentIds(@Param("parentIds") List<Long> parentIds);
}
