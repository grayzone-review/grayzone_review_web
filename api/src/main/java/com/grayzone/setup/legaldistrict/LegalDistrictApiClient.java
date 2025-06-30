package com.grayzone.setup.legaldistrict;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LegalDistrictApiClient {
  private final RestClient restClient;
  private final LegalDistrictApiProperties properties;

  public LegalDistrictApiClient(LegalDistrictApiProperties properties) {
    this.properties = properties;
    this.restClient = RestClient.builder()
      .baseUrl(properties.getBaseUrl())
      .build();
  }

  public LegalDistrictsApiResponse getAllLegalDistricts(int page, int perPage) {
    return restClient.get()
      .uri(uriBuilder -> uriBuilder
        .path(properties.getPath())
        .queryParam("page", page)
        .queryParam("perPage", perPage)
        .build()
      )
      .header("Authorization", properties.getKey())
      .retrieve()
      .body(LegalDistrictsApiResponse.class);
  }
}
