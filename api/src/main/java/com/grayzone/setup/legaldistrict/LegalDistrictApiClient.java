package com.grayzone.setup.legaldistrict;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
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
      .onStatus(
        status -> status.is4xxClientError() || status.is5xxServerError(),
        (request, response) -> {
          String errorBody = response.getBody().toString();
          log.error("API Error Body: {}", errorBody);
        }
      )
      .body(LegalDistrictsApiResponse.class);
  }
}
