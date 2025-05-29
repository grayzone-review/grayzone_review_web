package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.review.entity.ReviewComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateReviewCommentResponseDto {
  private Long id;
  private String comment;
  private String authorName;
  private LocalDateTime createdAt;
  private boolean isSecret;
  private boolean isVisible;
  private int replyCount;

  public static CreateReviewCommentResponseDto from(ReviewComment reviewComment, boolean isVisible, int replyCount) {
    return CreateReviewCommentResponseDto.builder()
      .id(reviewComment.getId())
      .comment(reviewComment.getComment())
      .authorName(reviewComment.getAuthorName())
      .createdAt(reviewComment.getCreatedAt())
      .isSecret(reviewComment.isSecret())
      .isVisible(isVisible)
      .replyCount(replyCount)
      .build();
  }
}
