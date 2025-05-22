package com.grayzone.common;

public class CommonDataResponse<T> extends CommonResponse {
  private final T data;

  public CommonDataResponse(T data) {
    super(true, "성공");
    this.data = data;
  }

  public static <T> CommonDataResponse<T> from(T data) {
    return new CommonDataResponse<>(data);
  }
}
