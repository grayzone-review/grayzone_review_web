package com.grayzone.domain.review.dto.request;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewComment;
import com.grayzone.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class CreateReviewCommentRequestDto {

  @NotBlank
  @Length(max = 200)
  private String comment;
  private boolean isSecret;

  public ReviewComment toEntity(CompanyReview companyReview, User user) {
    return ReviewComment.builder()
      .comment(comment)
      .isSecret(isSecret)
      .companyReview(companyReview)
      .user(user)
      .build();
  }
}
