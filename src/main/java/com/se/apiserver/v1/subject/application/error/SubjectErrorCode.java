package com.se.apiserver.v1.subject.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum SubjectErrorCode implements ErrorCode {

  NO_SUCH_SUBJECT(400, "SE01", "존재하지 않는 교과"),
  DUPLICATED_SUBJECT(400, "SE02", "교과 코드 중복"),
  INVALID_GRADE(400, "SE03", "유효하지 않은 대상학년"),
  INVALID_SEMESTER(400, "SE04", "유효하지 않은 개설학기"),
  INVALID_CREDIT(400, "SE05", "유효하지 않은 학점");

  private final int status;
  private final String code;
  private final String message;

  SubjectErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
