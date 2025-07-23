package com.grayzone.common;

import lombok.Getter;

@Getter
public class ResponseErrorDto extends ResponseDto {
  private final int code;

  public ResponseErrorDto(String message, int code) {
    super(false, message);
    this.code = code;
  }

  public static ResponseErrorDto from(String message, int code) {
    return new ResponseErrorDto(message, code);
  }
}
