package com.grayzone.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UpError {

  // 회원 관련 오류 (2000번대)
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", 2010),
  NICKNAME_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다.", 2011),

  // 인증 관련 오류 (3000번대)
  UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "비회원입니다.", 3001),
  ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 3002),
  REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 3003),
  ADMIN_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.", 3004),

  // OAuth 관련 오류 (3100번대)
  OAUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", 3101),
  OAUTH_UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 공급자입니다.", 3102),

  // 회사 관련 오류 (4000번대)
  COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회사가 존재하지 않습니다.", 4001),
  REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 동네가 존재하지 않습니다.", 4002),

  // 리뷰 관련 오류 (4100번대)
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 존재하지 않습니다.", 4101),
  REVIEW_ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 리뷰입니다.", 4102),
  REVIEW_NOT_LIKED(HttpStatus.CONFLICT, "좋아요하지 않은 리뷰입니다.", 4103),

  // 댓글 관련 오류 (4200번대)
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다.", 4201),
  COMMENT_NO_PERMISSION(HttpStatus.FORBIDDEN, "답글을 작성할 수 없습니다.", 4202),

  // 팔로우 관련 오류 (4300번대)
  FOLLOW_ALREADY(HttpStatus.CONFLICT, "이미 팔로우한 회사입니다.", 4301),
  FOLLOW_NOT_EXIST(HttpStatus.CONFLICT, "팔로우하지 않은 회사입니다.", 4302),

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", 4400),

  // 서버 및 파일 관련 오류 (5000번대)
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", 5000);

  private final HttpStatus status;
  private final String message;
  private final int code;
}