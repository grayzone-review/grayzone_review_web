package com.grayzone.domain.user.service;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.user.entity.FollowCompany;
import com.grayzone.domain.user.entity.User;
import com.grayzone.domain.user.repository.FollowCompanyRepository;
import com.grayzone.global.exception.UpError;
import com.grayzone.global.exception.UpException;
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
      .orElseThrow(() -> new UpException(UpError.COMPANY_NOT_FOUND));

    if (followCompanyRepository.existsByCompanyAndUser(company, user)) {
      throw new UpException(UpError.FOLLOW_ALREADY);
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
      .orElseThrow(() -> new UpException(UpError.COMPANY_NOT_FOUND));

    FollowCompany followCompany = followCompanyRepository.findByCompanyAndUser(company, user)
      .orElseThrow(() -> new UpException(UpError.FOLLOW_NOT_EXIST));

    followCompanyRepository.delete(followCompany);
  }
}
