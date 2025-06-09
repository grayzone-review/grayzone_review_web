package com.grayzone.domain.company.repository;

public interface CompanySearchResult {
  Long getId();

  String getCompanyName();

  String getSiteFullAddress();

  String getRoadNameAddress();

  double getDistance();

}
