package com.grayzone.company.repository;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CompanyRepositoryTest {

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  void testFindByKeywordWithPaging() {
    String keyword = "삼겹살";
    double lat = 37.5665;
    double lng = 126.9780;
    Pageable pageable = PageRequest.of(0, 10);

    Page<Company> results = companyRepository.findByKeywordOrderByDistance(
      keyword, lat, lng, pageable
    );

    log.info("couts {}", results.getTotalElements());
    for (Company result : results) {
      log.info("result {}", result.getBusinessName());
    }
  }
}
