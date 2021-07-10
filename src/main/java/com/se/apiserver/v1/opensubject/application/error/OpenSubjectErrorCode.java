package com.se.apiserver.v1.opensubject.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum OpenSubjectErrorCode implements ErrorCode {

  NO_SUCH_OPEN_SUBJECT(400, "OSE01", "존재하지 않는 개설 교과"),
  DUPLICATED_OPEN_SUBJECT(400, "OSE02", "해당 교과는 이미 해당 시간표에 개설됨"),
  INVALID_NUMBER_OF_DIVISION(400, "OSE03", "분반 수가 0 이하"),
  INVALID_TEACHING_TIME_PER_WEEK(400, "OSE04", "주간 수업 시간이 0 이하");

  private final int status;
  private final String code;
  private final String message;

  OpenSubjectErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
