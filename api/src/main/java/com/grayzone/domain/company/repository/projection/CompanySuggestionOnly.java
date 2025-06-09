package com.grayzone.domain.company.repository.projection;

public interface CompanySuggestionOnly {
  
  Long getId();

  String getCompanyName();

  String getSiteFullAddress();

  String getRoadNameAddress();
}
