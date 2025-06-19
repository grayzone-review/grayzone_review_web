package com.grayzone.domain.review.dto.request;

import com.grayzone.domain.company.entity.Company;
import com.grayzone.domain.review.RatingCategory;
import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewRating;
import com.grayzone.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

@Setter
public class CreateCompanyReviewRequestDto {
  
  private String advantagePoint;

  private String disadvantagePoint;

  private String managementFeedback;
  @NotBlank
  private String jobRole;
  @Length(min = 2, max = 10)
  private String employmentPeriod;

  private Map<String, Double> ratings;

  public String getSummarizeSourceText() {
    return advantagePoint + disadvantagePoint + managementFeedback;
  }

  public CompanyReview toCompanyReview(
    Company company,
    User user,
    String title
  ) {
    return CompanyReview.builder()
      .title(title)
      .advantagePoint(advantagePoint)
      .disadvantagePoint(disadvantagePoint)
      .managementFeedback(managementFeedback)
      .jobRole(jobRole)
      .employmentPeriod(employmentPeriod)
      .company(company)
      .user(user)
      .build();
  }

  public List<ReviewRating> toReviewRatings(CompanyReview companyReview) {
    if (ratings.size() != RatingCategory.values().length) {
      throw new IllegalArgumentException("Rating category array size is not correct");
    }

    return ratings.entrySet().stream()
      .map(entry -> {
        String categoryLabel = entry.getKey();
        Double rating = entry.getValue();

        RatingCategory category = RatingCategory.fromLabel(categoryLabel)
          .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryLabel));

        return ReviewRating.builder()
          .rating(rating)
          .category(category)
          .companyReview(companyReview)
          .build();
      })
      .toList();
  }

}
