package com.grayzone.domain.legaldistrict.repository;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.projection.LegalDistrictPrefixOnly;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LegalDistrictRepository extends JpaRepository<LegalDistrict, Long> {
  @Query(value = """
        SELECT ld.id, SUBSTRING_INDEX(ld.address, ' ', 3) AS legal_district
        FROM legal_districts ld
        WHERE ld.address LIKE %:keyword%
        GROUP BY legal_district, id
    """, nativeQuery = true)
  Slice<LegalDistrictPrefixOnly> findLegalDistrictByKeyword(
    @Param("keyword") String keyword,
    Pageable pageable
  );
}
