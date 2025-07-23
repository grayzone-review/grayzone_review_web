package com.grayzone.global.exception;

import com.grayzone.common.ResponseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
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

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ResponseErrorDto> handleDataAccessException(DataAccessException exception) {
    log.error(exception.getMessage(), exception);

    return ResponseEntity
      .status(UpError.SERVER_ERROR.getStatus())
      .body(ResponseErrorDto.from(UpError.SERVER_ERROR.getMessage(), UpError.SERVER_ERROR.getCode()));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ResponseErrorDto> handleAuthenticationException(AuthenticationException exception) {
    log.error(exception.getMessage(), exception);

    return ResponseEntity
      .status(UpError.UNAUTHORIZED.getStatus())
      .body(ResponseErrorDto.from(UpError.UNAUTHORIZED.getMessage(), UpError.UNAUTHORIZED.getCode()));
  }
}