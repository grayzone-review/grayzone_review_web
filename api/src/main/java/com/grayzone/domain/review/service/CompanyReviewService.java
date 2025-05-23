package com.grayzone.domain.review.service;

import com.grayzone.domain.company.repository.CompanyRepository;
import com.grayzone.domain.review.dto.CompanyReviewListResponseDto;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.repository.CompanyReviewRepository;
import com.grayzone.domain.review.repository.ReviewLikeRepository;
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

  public CompanyReviewListResponseDto getReviewsByCompanyId(Long companyId, Pageable pageable) {
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
      : reviewLikeRepository.findReviewIdsLikedByUser(1L, reviewIds);

    return CompanyReviewListResponseDto.from(
      reviewPage,
      userLikedReviewIds
    );
  }
}
