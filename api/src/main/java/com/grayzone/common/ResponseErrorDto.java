package com.grayzone.common;

public class ResponseErrorDto extends ResponseDto {
  public ResponseErrorDto(String message) {
    super(false, message);
  }

  public static ResponseErrorDto from(String message) {
    return new ResponseErrorDto(message);
  }
}
