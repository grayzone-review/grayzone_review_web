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

    public static ReplyResponseDto from(ReviewComment comment, Long viewerId) {
      boolean isVisible = comment.isVisibleTo(viewerId);

      return ReplyResponseDto.builder()
        .id(comment.getId())
        .comment(isVisible ? comment.getComment() : null)
        .authorName(isVisible ? comment.getAuthorName() : null)
        .isSecret(comment.isSecret())
        .isVisible(isVisible)
        .build();
    }
  }
}
