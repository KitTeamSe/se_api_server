package com.se.apiserver.v1.common.exception;

import com.se.apiserver.v1.common.domain.error.ErrorCode;

public abstract class BusinessException extends RuntimeException {

  private ErrorCode errorCode;

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public BusinessException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}
