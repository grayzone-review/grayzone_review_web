package com.grayzone.legaldistrict.repository;

import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class LegalDistrictRepositoryTest {

  @Autowired
  private LegalDistrictRepository legalDistrictRepository;

  @Test
  public void findLegalDistrictsByKeyword() {
    String keyword = "기장군";
    Pageable pageable = PageRequest.of(0, 10);

    legalDistrictRepository.findLegalDistrictByKeyword(keyword, pageable).stream()
      .forEach(legalDistrict -> {
        log.info(legalDistrict.getLegalDistrict());
      });
  }
}
