package com.grayzone.domain.legaldistrict.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "legal_districts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LegalDistrict {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String address;

  @Builder
  public LegalDistrict(String address) {
    this.address = address;
  }
}
