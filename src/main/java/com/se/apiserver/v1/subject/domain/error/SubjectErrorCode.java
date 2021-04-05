package com.se.apiserver.v1.subject.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum SubjectErrorCode implements ErrorCode {

  NO_SUCH_SUBJECT(400, "SE01", "존재하지 않는 교과"),
  DUPLICATED_SUBJECT(401, "SE02", "교과 코드 중복");

  private final int status;
  private final String code;
  private final String message;

  SubjectErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
