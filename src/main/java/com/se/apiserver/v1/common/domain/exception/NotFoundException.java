package com.se.apiserver.v1.common.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 클라이언트가 요청한 리소스가 존재하지 않을 때 발생하는 예외.
 */
public class NotFoundException extends SeException {

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public NotFoundException(String message,
      Throwable cause) {
    super(HttpStatus.NOT_FOUND, message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(HttpStatus.NOT_FOUND, cause);
  }
}