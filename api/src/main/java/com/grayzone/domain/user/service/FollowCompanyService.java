package com.grayzone.domain.user.service;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.user.entity.FollowCompany;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.FollowCompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowCompanyService {

  private final FollowCompanyRepository followCompanyRepository;
  private final CompanyRepository companyRepository;

  @Transactional
  public void createFollowCompany(Long companyId, User user) {
    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new EntityNotFoundException("Company not found"));

    if (followCompanyRepository.existsByCompanyAndUser(company, user)) {
      throw new IllegalArgumentException("User request already followed company");
    }

    FollowCompany followCompany = FollowCompany.builder()
      .company(company)
      .user(user)
      .build();

    followCompanyRepository.save(followCompany);
  }

  @Transactional
  public void deleteFollowCompany(Long companyId, User user) {
    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new EntityNotFoundException("Company not found"));

    FollowCompany followCompany = followCompanyRepository.findByCompanyAndUser(company, user)
      .orElseThrow(() -> new EntityNotFoundException("User request to not followed company"));

    followCompanyRepository.delete(followCompany);
  }
}
