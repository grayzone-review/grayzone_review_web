package com.grayzone.global.exception;

import com.grayzone.common.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UpException.class)
  public ResponseEntity<ResponseErrorDto> handleGlobalException(UpException exception) {
    UpError error = exception.getError();

    return ResponseEntity
      .status(error.getStatus())
      .body(ResponseErrorDto.from(error.getMessage(), error.getCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorDto> handleGlobalException(MethodArgumentNotValidException exception) {
    List<String> errorMessages = exception.getBindingResult().getFieldErrors()
      .stream()
      .map(FieldError::getDefaultMessage)
      .toList();

    String errorMessage = errorMessages.isEmpty()
      ? UpError.INVALID_REQUEST.getMessage()
      : errorMessages.getFirst();

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ResponseErrorDto.from(errorMessage, UpError.INVALID_REQUEST.getCode()));
  }
}