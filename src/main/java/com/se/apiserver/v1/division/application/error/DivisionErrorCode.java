package com.se.apiserver.v1.division.application.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DivisionErrorCode implements ErrorCode {

  NO_SUCH_DIVISION(400, "DVE01", "존재하지 않는 분반"),
  INVALID_DEPLOYED_TEACHING_TIME(400, "DVE02", "주간 수업 시간이 0 이하거나 비어있음"),
  LESS_DEPLOYED_TEACHING_TIME(400, "DVE03", "배치된 시간이 주간 수업 시간보다 적음"),
  EXCEEDED_DEPLOYED_TEACHING_TIME(400, "DVE04", "배치된 시간이 주간 수업 시간보다 많음"),
  DUPLICATED_DIVISION_NUMBER(400, "DVE05", "해당 개설 교과에 동일한 분반이 존재");

  private final int status;
  private final String code;
  private final String message;

  DivisionErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
