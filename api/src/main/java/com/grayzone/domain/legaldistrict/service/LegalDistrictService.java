package com.grayzone.domain.legaldistrict.service;

import com.grayzone.domain.legaldistrict.repository.LegalDistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LegalDistrictService {
  private final LegalDistrictRepository legalDistrictRepository;

  
}
