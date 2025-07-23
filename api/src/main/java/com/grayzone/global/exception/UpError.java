package com.grayzone.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UpError {

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다.", 1001),
  NICKNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다.", 1002),
  MAIN_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동네가 존재하지 않습니다.", 1003),

  UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "비회원입니다.", 1004),
  ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 1005),
  REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 1006),
  ADMIN_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.", 1007),

  OAUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 1101),
  OAUTH_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 공급자입니다.", 1102),

  COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회사가 존재하지 않습니다.", 2001),

  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다..", 3001),
  REVIEW_ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 리뷰입니다.", 3002),
  REVIEW_NOT_LIKED(HttpStatus.CONFLICT, "좋아요하지 않은 리뷰입니다.", 3003),

  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다.", 4001),
  COMMENT_NO_PERMISSION(HttpStatus.FORBIDDEN, "답글을 작성할 수 없습니다.", 4002),

  FOLLOW_ALREADY(HttpStatus.CONFLICT, "이미 팔로우한 회사입니다.", 5001),
  FOLLOW_NOT_EXIST(HttpStatus.CONFLICT, "팔로우하지 않은 회사입니다.", 5002),

  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", 6000),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다.", 6001);

  private final HttpStatus status;
  private final String message;
  private final int code;
}
