package com.grayzone.domain.company.repository.projection;

public interface CompanySearchOnly {
  Long getId();

  String getCompanyName();

  String getSiteFullAddress();

  String getRoadNameAddress();

  double getDistance();

}
