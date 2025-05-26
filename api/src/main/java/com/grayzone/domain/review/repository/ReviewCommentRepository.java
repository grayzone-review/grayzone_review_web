package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.ReviewComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

  Page<ReviewComment> findByCompanyReviewId(Long reviewId, Pageable pageable);
}
