package com.se.apiserver.v1.common.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Unique value가 이미 존재하는 경우 발생하는 예외.
 * 메시지에 이유를 포함하여 반환하기 바람니다.
 */
public class UniqueValueAlreadyExistsException extends SeException {

  public UniqueValueAlreadyExistsException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public UniqueValueAlreadyExistsException(String message, Throwable cause) {
    super(HttpStatus.BAD_REQUEST, message, cause);
  }

  public UniqueValueAlreadyExistsException(Throwable cause) {
    super(HttpStatus.BAD_REQUEST, cause);
  }
}