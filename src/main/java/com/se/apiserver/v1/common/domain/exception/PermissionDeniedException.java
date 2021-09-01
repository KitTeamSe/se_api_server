package com.se.apiserver.v1.common.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 인증된 사용자가 권한이 없는 리소스에 접근할 때 발생하는 예외.
 */
public class PermissionDeniedException extends SeException {

  public PermissionDeniedException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

  public PermissionDeniedException(String message, Throwable cause) {
    super(HttpStatus.FORBIDDEN, message, cause);
  }

  public PermissionDeniedException(Throwable cause) {
    super(HttpStatus.FORBIDDEN, cause);
  }
}