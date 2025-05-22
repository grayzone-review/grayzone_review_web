package com.grayzone.domain.user.repository;

import com.grayzone.domain.user.entity.FollowCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowCompanyRepository extends JpaRepository<FollowCompany, Long> {
  boolean existsByUserIdAndCompanyId(Long userId, Long companyId);
}
