package com.grayzone.domain.review.dto;

import com.grayzone.domain.review.entity.ReviewComment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewCommentListResponseDto {

  private final List<ReviewCommentResponseDto> comments;
  private final boolean hasNext;
  private final int currentPage;

  public static ReviewCommentListResponseDto from(Page<ReviewComment> commentsPage, Long viewerId) {
    List<ReviewCommentResponseDto> reviewCommentResponseDtos = commentsPage.getContent().stream()
      .map((reviewComment) -> ReviewCommentResponseDto.from(reviewComment, viewerId))
      .toList();

    return ReviewCommentListResponseDto.builder()
      .comments(reviewCommentResponseDtos)
      .hasNext(commentsPage.hasNext())
      .currentPage(commentsPage.getNumber())
      .build();
  }

  @Getter
  @Builder
  public static class ReviewCommentResponseDto {
    private Long id;
    private String comment;
    private String authorName;
    private LocalDateTime createdAt;
    private boolean isSecret;
    private boolean isVisible;

    public static ReviewCommentResponseDto from(ReviewComment reviewComment, Long viewerId) {
      boolean isVisible = reviewComment.isVisibleTo(viewerId);

      return ReviewCommentResponseDto.builder()
        .id(reviewComment.getId())
        .comment(isVisible ? reviewComment.getComment() : null)
        .authorName(isVisible ? reviewComment.getAuthorName() : null)
        .createdAt(isVisible ? reviewComment.getCreatedAt() : null)
        .isSecret(reviewComment.isSecret())
        .isVisible(isVisible)
        .build();
    }
  }
}
