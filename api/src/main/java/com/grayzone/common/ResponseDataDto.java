package com.grayzone.common;

import lombok.Getter;

@Getter
public class ResponseDataDto<T> extends ResponseDto {
  private final T data;

  public ResponseDataDto(T data) {
    super(true, "성공");
    this.data = data;
  }

  public ResponseDataDto(T data, String message) {
    super(true, message);
    this.data = data;
  }

  public static <T> ResponseDataDto<T> from(T data) {
    return new ResponseDataDto<>(data);
  }
}
