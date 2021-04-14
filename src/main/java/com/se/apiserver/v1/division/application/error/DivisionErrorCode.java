package com.se.apiserver.v1.division.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum DivisionErrorCode implements ErrorCode {

  NO_SUCH_DIVISION(400, "DVE01", "존재하지 않는 분반"),
  INVALID_DIVISION(401, "DVE02", "분반 수가 0 이하 혹은 개설교과의 분반 수 보다 큼"),
  INVALID_DEPLOYED_TEACHING_TIME(402, "DVE03", "주간 수업 시간이 0 이하");

  private final int status;
  private final String code;
  private final String message;

  DivisionErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
