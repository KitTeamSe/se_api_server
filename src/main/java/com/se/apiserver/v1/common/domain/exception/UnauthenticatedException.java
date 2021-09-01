package com.se.apiserver.v1.common.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * 인증되지 않은 사용자가 인증이 필요한 리소스에 접근할 때 발생하는 예외.
 */
public class UnauthenticatedException extends SeException {

  // HTTP Status에서는 Unauthorized이지만, 의미상으로는 unauthenticated가 맞습니다.
  // ref : https://developer.mozilla.org/ko/docs/Web/HTTP/Status
  public UnauthenticatedException(String message) {
    super(HttpStatus.UNAUTHORIZED, message);
  }

  public UnauthenticatedException(String message, Throwable cause) {
    super(HttpStatus.UNAUTHORIZED, message, cause);
  }

  public UnauthenticatedException(Throwable cause) {
    super(HttpStatus.UNAUTHORIZED, cause);
  }
}