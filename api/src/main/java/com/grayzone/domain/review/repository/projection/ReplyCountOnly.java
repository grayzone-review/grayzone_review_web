package com.grayzone.domain.review.repository.projection;

public interface ReplyCountOnly {
  Long getReviewId();

  Integer getCount();
}
