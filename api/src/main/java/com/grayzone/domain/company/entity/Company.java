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
@Table(name = "companies")
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
  private Double latitude;
  private Double longitude;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  private List<CompanyReview> companyReviews = new ArrayList<>();

  public Double calculateDistanceFrom(
    Double targetLatitude,
    Double targetLongitude
  ) {
    final int EARTH_RADIUS_KM = 6371;

    double lat1Rad = Math.toRadians(this.latitude);
    double lat2Rad = Math.toRadians(targetLatitude);
    double deltaLat = Math.toRadians(targetLatitude - this.latitude);
    double deltaLng = Math.toRadians(targetLongitude - this.longitude);

    double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
      + Math.cos(lat1Rad) * Math.cos(lat2Rad)
      * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
  }

}
