package com.grayzone.global.exception;

import com.grayzone.common.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorDto> handleGlobalException(Exception exception) {
    String errorMessage = exception.getMessage();

    log.error(exception.getClass().getName());

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ResponseErrorDto.from(errorMessage));
  }
}