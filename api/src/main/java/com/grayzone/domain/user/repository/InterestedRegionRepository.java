package com.grayzone.domain.user.repository;

import com.grayzone.domain.user.entity.InterestedRegion;
import com.grayzone.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestedRegionRepository extends JpaRepository<InterestedRegion, Long> {

  @Query("""
      SELECT ir FROM InterestedRegion ir
      JOIN FETCH ir.legalDistrict
      WHERE ir.user = :user
    """)
  List<InterestedRegion> findAllByUserWithLegalDistrict(@Param("user") User user);
}
