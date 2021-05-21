package com.se.apiserver.v1.post.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum PostSearchErrorCode implements ErrorCode {

  INVALID_SEARCH_KEYWORD(400, "PSE01", "검색 키워드가 너무 짧음"),
  NO_SUCH_SEARCH_TYPE(400, "PSE02", "존재하지 않는 검색 타입");

  private int status;
  private final String code;
  private final String message;

  PostSearchErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
