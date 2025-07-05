package com.grayzone.setup.legaldistrict;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalDistrictSetupService {
  private final LegalDistrictRepository legalDistrictRepository;
  private final LegalDistrictApiClient legalDistrictApiClient;

  @Transactional
  public void setupLegalDistricts() {
    int currentPage = 1;
    int perPage = 3000;
    int totalCount = 0;

    do {
      LegalDistrictsApiResponse legalDistrictsApiResponse = legalDistrictApiClient.getAllLegalDistricts(currentPage, perPage);
      totalCount = legalDistrictsApiResponse.getTotalCount();
      currentPage++;

      legalDistrictsApiResponse.getData().stream()
        .filter(legalDistrict -> legalDistrict.getDeletedDate() == null)
        .filter(ld -> ld.getProvince() != null && ld.getCity() != null && ld.getTown() != null && ld.getVillage() == null)
        .map(legalDistrict -> {
          String address = Stream.of(
              legalDistrict.getProvince(),
              legalDistrict.getCity(),
              legalDistrict.getTown()
            )
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
          return new LegalDistrict(address);
        }).forEach(legalDistrictRepository::save);

    } while (currentPage * perPage < totalCount);
  }
}
