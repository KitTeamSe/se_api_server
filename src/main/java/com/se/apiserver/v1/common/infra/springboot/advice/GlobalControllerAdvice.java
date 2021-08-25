package com.se.apiserver.v1.common.infra.springboot.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.se.apiserver.v1.common.domain.error.ErrorCode;
import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.ErrorResponse;
import com.se.apiserver.v1.common.infra.logging.SeLogger;
import com.se.apiserver.v1.common.infra.logging.standard.SeStandardLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

  private final SeLogger logger;

  public GlobalControllerAdvice() {
    this.logger = new SeStandardLogger();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BindException.class)
  protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.METHOD_NOT_ALLOWED);
    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.HANDLE_ACCESS_DENIED);
    return new ResponseEntity<>(response, HttpStatus.valueOf(GlobalErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    countExceptionAndLog(e);
    ErrorCode errorCode = e.getErrorCode();
    final ErrorResponse response = ErrorResponse.of(errorCode);
    return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
  }

  @ExceptionHandler(JsonParseException.class)
  protected ResponseEntity<ErrorResponse> handleJsonParseException(final JsonParseException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INVALID_JSON_INPUT);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidFormatException.class)
  protected ResponseEntity<ErrorResponse> handleInvalidFormatException(final InvalidFormatException e) {
    countExceptionAndLog(e);
    final ErrorResponse response = ErrorResponse.of(GlobalErrorCode.INVALID_ENUM_INPUT);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private void countExceptionAndLog(final Exception e) {
    logger.error(e.getClass().getSimpleName(), e.getMessage());
    logger.debug(e.getClass().getSimpleName(), e.getMessage(), e);
  }
}
