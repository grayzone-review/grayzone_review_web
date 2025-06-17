package com.grayzone.domain.review.service;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.review.dto.request.CreateCompanyReviewRequestDto;
import com.grayzone.domain.review.dto.response.CompanyReviewsResponseDto;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewLikeRepository;
import com.grayzone.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyReviewService {

  private final CompanyReviewRepository companyReviewRepository;
  private final CompanyRepository companyRepository;
  private final ReviewLikeRepository reviewLikeRepository;

  public CompanyReviewsResponseDto getReviewsByCompanyId(Long companyId, Long userId, Pageable pageable) {
    if (!companyRepository.existsById(companyId)) {
      throw new EntityNotFoundException("Company not found");
    }

    Page<CompanyReview> reviewPage = companyReviewRepository.findByCompanyId(companyId, pageable);

    List<Long> reviewIds = reviewPage.getContent()
      .stream()
      .map(CompanyReview::getId)
      .toList();

    Set<Long> userLikedReviewIds = reviewIds.isEmpty()
      ? Set.of()
      : reviewLikeRepository.findReviewIdsLikedByUser(userId, reviewIds);

    return CompanyReviewsResponseDto.from(
      reviewPage,
      userLikedReviewIds
    );
  }

  public void createCompanyReview(
    Long companyId,
    CreateCompanyReviewRequestDto requestDto,
    User user
  ) {
    Company company = companyRepository.findById(companyId)
      .orElseThrow(() -> new EntityNotFoundException("Company not found"));


  }
}
