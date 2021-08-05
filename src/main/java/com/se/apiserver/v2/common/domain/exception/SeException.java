package com.se.apiserver.v2.common.domain.exception;

import org.springframework.http.HttpStatus;

public class SeException extends RuntimeException {

  private final HttpStatus status;

  public SeException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public SeException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public SeException(HttpStatus status, Throwable cause) {
    super(cause);
    this.status = status;
  }

  public HttpStatus getHttpStatus() {
    return status;
  }
}