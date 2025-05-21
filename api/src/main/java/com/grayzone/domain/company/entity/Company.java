package com.grayzone.domain.company.entity;

import com.grayzone.domain.review.entity.CompanyReview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="companies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String openServiceName;
  private String openServiceId;
  private LocalDateTime permissionDate;
  private String businessStatus;
  private String sitePhoneNumber;
  private String siteFullAddress;
  private String roadNameAddress;
  private String roadNameZipCode;
  private String businessName;
  private Double xCoordinate;
  private Double yCoordinate;

  @OneToMany(mappedBy = "company")
  private List<CompanyReview> companyReviews = new ArrayList<>();
}
