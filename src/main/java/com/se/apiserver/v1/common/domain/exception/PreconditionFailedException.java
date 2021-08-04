package com.se.apiserver.v1.common.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 메소드 호출시 전제조건이 충족되지 않은 경우의 예외.
 */
public class PreconditionFailedException extends SeException {

  public PreconditionFailedException( String message) {
    super(HttpStatus.PRECONDITION_FAILED, message);
  }

  public PreconditionFailedException(String message, Throwable cause) {
    super(HttpStatus.PRECONDITION_FAILED, message, cause);
  }

  public PreconditionFailedException(Throwable cause) {
    super(HttpStatus.PRECONDITION_FAILED, cause);
  }
}
