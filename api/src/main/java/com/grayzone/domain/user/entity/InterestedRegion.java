package com.grayzone.domain.user.entity;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "interested_regions")
@Getter
public class InterestedRegion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "interested_legal_district_id")
  private LegalDistrict legalDistrict;
}
