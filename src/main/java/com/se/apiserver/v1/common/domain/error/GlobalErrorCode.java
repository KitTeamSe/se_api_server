package com.se.apiserver.v1.common.domain.error;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements ErrorCode {

  INVALID_INPUT_VALUE(400, "GE01", "올바르지 않은 입력"),
  METHOD_NOT_ALLOWED(405, "GE02", "허용되지 않은 메소드"),
  HANDLE_ACCESS_DENIED(403, "GE03", "권한 없음"),
  INVALID_JSON_INPUT(400, "GE04", "올바르지 않은 JSON 입력");

  private final String code;
  private final String message;
  private int status;

  GlobalErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

}