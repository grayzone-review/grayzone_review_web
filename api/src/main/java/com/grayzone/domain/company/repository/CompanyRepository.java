package com.grayzone.domain.company.repository;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.projection.CompanySearchOnly;
import com.grayzone.domain.company.repository.projection.CompanySuggestionOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  @Query(value = """
        SELECT c.id, c.business_name AS company_name, c.site_full_address, c.road_name_address,
            (6371 * acos(
                cos(radians(:latitude)) * cos(radians(c.latitude)) *
                cos(radians(c.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(c.latitude))
            )) AS distance
        FROM companies c
        WHERE MATCH(c.business_name) AGAINST(:keyword IN BOOLEAN MODE)
        ORDER BY distance IS NULL, distance ASC
    """, nativeQuery = true)
  Page<CompanySearchOnly> searchByKeywordOrderByDistance(
    @Param("keyword") String keyword,
    @Param("latitude") Double latitude,
    @Param("longitude") Double longitude,
    Pageable pageable
  );

  @Query(value = """
        SELECT c.id, c.business_name AS company_name, c.site_full_address, c.road_name_address
        FROM companies c
        WHERE MATCH(c.business_name) AGAINST(:keyword IN BOOLEAN MODE)
        ORDER BY (latitude IS NULL OR longitude IS NULL), (6371 * acos(
                cos(radians(:latitude)) * cos(radians(c.latitude)) *
                cos(radians(c.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(c.latitude))
            )) ASC
    """, nativeQuery = true)
  Slice<CompanySuggestionOnly> suggestByKeywordOrderByDistance(
    @Param("keyword") String keyword,
    @Param("latitude") Double latitude,
    @Param("longitude") Double longitude,
    Pageable pageable
  );
}
