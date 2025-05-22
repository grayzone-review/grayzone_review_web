package com.grayzone.common;

import lombok.Getter;

@Getter
public abstract class ResponseDto {
  private final boolean success;
  private final String message;

  protected ResponseDto(boolean success, String message) {
    this.success = success;
    this.message = message;
  }
}
