package com.grayzone.domain.review.dto.request;

import com.grayzone.domain.review.entity.CompanyReview;
import com.grayzone.domain.review.entity.ReviewComment;
import com.grayzone.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
public class CreateReplyRequestDto {
  @NotBlank
  @Length(max = 200)
  private String comment;

  @NotNull
  private boolean isSecret;

  public ReviewComment toEntity(CompanyReview companyReview, User user, ReviewComment parentComment) {
    return ReviewComment.builder()
      .comment(comment)
      .isSecret(isSecret)
      .companyReview(companyReview)
      .parent(parentComment)
      .user(user)
      .build();
  }
}
