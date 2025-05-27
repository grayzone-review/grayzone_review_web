package com.grayzone.domain.review.dto.request;

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
}
