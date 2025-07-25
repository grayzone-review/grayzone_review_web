package com.grayzone.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInteractionCountsResponseDto {
  private long myReviewCount;
  private long likeOrCommentReviewCount;
  private long followCompanyCount;
}
