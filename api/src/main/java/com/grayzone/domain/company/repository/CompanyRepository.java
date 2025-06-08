package com.grayzone.domain.company.repository;

import com.grayzone.domain.company.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  @Query(value = """
        SELECT *,
            (6371 * acos(
                cos(radians(:latitude)) * cos(radians(c.latitude)) *
                cos(radians(c.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(c.latitude))
            )) AS distance
        FROM companies c
        WHERE c.business_name LIKE %:keyword%
        ORDER BY distance IS NULL, distance ASC
    """, nativeQuery = true)
  Page<Company> findByKeywordOrderByDistance(
    @Param("keyword") String keyword,
    @Param("latitude") Double latitude,
    @Param("longitude") Double longitude,
    Pageable pageable
  );
}
