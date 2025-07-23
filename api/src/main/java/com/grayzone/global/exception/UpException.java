package com.grayzone.global.exception;

import lombok.Getter;

@Getter
public class UpException extends RuntimeException {
  private final UpError error;

  public UpException(UpError error) {

    super(error.getMessage());
    this.error = error;
  }
}
