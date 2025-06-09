package com.grayzone.domain.user.repository;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.user.entity.FollowCompany;
import com.grayzone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowCompanyRepository extends JpaRepository<FollowCompany, Long> {
  boolean existsByUserIdAndCompanyId(Long userId, Long companyId);

  boolean existsByCompanyAndUser(Company company, User user);

  Optional<FollowCompany> findByCompanyAndUser(Company company, User user);

  @Query("""
      SELECT f.company.id
      FROM FollowCompany f
      WHERE f.user.id = :userId
        AND f.company.id IN :companyIds
    """)
  List<Long> findFollowedCompanyIdsByUserIdAndCompanyIds(@Param("userId") Long userId,
                                                         @Param("companyIds") List<Long> companyIds);
}
