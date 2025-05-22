package com.grayzone.common;

import lombok.Getter;

@Getter
public abstract class CommonResponse {
  private final boolean success;
  private final String message;

  protected CommonResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }
}
