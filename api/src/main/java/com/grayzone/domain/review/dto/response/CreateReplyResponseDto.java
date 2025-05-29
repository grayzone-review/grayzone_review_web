package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.review.entity.ReviewComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateReplyResponseDto {
  private Long id;
  private String comment;
  private String authorName;
  private LocalDateTime createdAt;
  private boolean isSecret;
  private boolean isVisible;

  public static CreateReplyResponseDto from(ReviewComment comment) {
    return CreateReplyResponseDto.builder()
      .id(comment.getId())
      .comment(comment.getComment())
      .authorName(comment.getAuthorName())
      .isSecret(comment.isSecret())
      .isVisible(true)
      .build();
  }
}
