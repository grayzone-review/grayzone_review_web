package com.grayzone.domain.review.repository;

import com.grayzone.domain.review.entity.CompanyReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {

}
