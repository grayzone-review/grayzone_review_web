package com.grayzone.domain.review.dto.response;

import com.grayzone.domain.review.entity.ReviewComment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReplyListResponseDto {
  private final List<ReplyResponseDto> replies;
  private final boolean hasNext;
  private final int currentPage;

  public static ReplyListResponseDto from(Page<ReviewComment> commentPages, Long viewerId) {
    List<ReplyResponseDto> replyResponseDtos = commentPages.getContent().stream()
      .map((comment) -> ReplyResponseDto.from(comment, viewerId))
      .toList();

    return ReplyListResponseDto.builder()
      .replies(replyResponseDtos)
      .hasNext(commentPages.hasNext())
      .currentPage(commentPages.getNumber())
      .build();
  }

  @Getter
  @Builder
  public static class ReplyResponseDto {
    private Long id;
    private String comment;
    private String authorName;
    private LocalDateTime createdAt;
    private boolean isSecret;
    private boolean isVisible;

    public static ReplyResponseDto from(ReviewComment reviewComment, Long viewerId) {
      boolean isVisible = reviewComment.isVisibleTo(viewerId);

      return ReplyResponseDto.builder()
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
