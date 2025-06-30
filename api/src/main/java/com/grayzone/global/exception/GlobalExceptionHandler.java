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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseErrorDto> handleGlobalException(Exception exception) {
    String errorMessage = exception.getMessage();

    log.error(exception.getClass().getName());

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(ResponseErrorDto.from(errorMessage));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorDto> handleGlobalException(MethodArgumentNotValidException exception) {
    List<String> errorMessages = exception.getBindingResult().getFieldErrors()
      .stream()
      .map(FieldError::getDefaultMessage)
      .toList();

    String errorMessage = errorMessages.isEmpty()
      ? "입력이 잘못되었습니다."
      : errorMessages.getFirst();

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ResponseErrorDto.from(errorMessage));
  }
}