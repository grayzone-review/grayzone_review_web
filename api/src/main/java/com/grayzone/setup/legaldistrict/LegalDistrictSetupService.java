package com.grayzone.setup.legaldistrict;

import com.grayzone.domain.legaldistrict.entity.LegalDistrict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalDistrictSetupService {
  private final LegalDistrictSetupRepository legalDistrictSetupRepository;
  private final LegalDistrictApiClient legalDistrictApiClient;

  public void setupLegalDistricts() {
    int perPage = 3000;

    LegalDistrictsApiResponse firstResponse = legalDistrictApiClient.getAllLegalDistricts(1, perPage);
    int totalCount = firstResponse.getTotalCount();
    int totalPage = (int) Math.ceil((double) totalCount / perPage);

    List<LegalDistrict> legalDistricts = new ArrayList<>(mappingLegalDistricts(firstResponse));
    List<CompletableFuture<List<LegalDistrict>>> futures = new ArrayList<>();

    for (int currentPage = 2; currentPage <= totalPage; currentPage++) {
      int page = currentPage;
      CompletableFuture<List<LegalDistrict>> future = CompletableFuture.supplyAsync(() -> {
        LegalDistrictsApiResponse response = legalDistrictApiClient.getAllLegalDistricts(page, perPage);
        return mappingLegalDistricts(response);
      });

      futures.add(future);
    }

    List<LegalDistrict> results = futures.stream()
      .map(CompletableFuture::join)
      .flatMap(List::stream)
      .toList();

    legalDistricts.addAll(results);
    legalDistricts = removeDuplicateAddresses(legalDistricts);
    
    try {
      legalDistrictSetupRepository.save(legalDistricts);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

  private List<LegalDistrict> removeDuplicateAddresses(List<LegalDistrict> legalDistricts) {
    return new ArrayList<>(new HashSet<>(legalDistricts));
  }

  private List<LegalDistrict> mappingLegalDistricts(LegalDistrictsApiResponse response) {
    return response.getData().stream()
      .filter(legalDistrict -> legalDistrict.getDeletedDate() == null)
      .filter(ld -> ld.getProvince() != null && ld.getCity() != null && ld.getTown() != null && ld.getVillage() == null)
      .map(legalDistrict -> {
        if (legalDistrict.getProvince().equals("강원도")) {
          legalDistrict.setProvince("강원특별자치도");
        }
        String city = legalDistrict.getCity();
        if (city.length() >= 5) {
          int siIndex = city.indexOf("시");

          if (siIndex != -1) {

            String beforeSi = city.substring(0, siIndex + 1);
            String afterSi = city.substring(siIndex + 1);

            legalDistrict.setCity(beforeSi);
            legalDistrict.setTown(afterSi);
          }
        }

        String address = Stream.of(
            legalDistrict.getProvince(),
            legalDistrict.getCity(),
            legalDistrict.getTown()
          )
          .filter(Objects::nonNull)
          .collect(Collectors.joining(" "));
        return new LegalDistrict(address);
      }).toList();
  }
}
