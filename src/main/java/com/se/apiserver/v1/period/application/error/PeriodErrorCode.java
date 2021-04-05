package com.se.apiserver.v1.period.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum PeriodErrorCode implements ErrorCode {

  NO_SUCH_PERIOD(400, "PE01", "존재하지 않는 교시"),
  DUPLICATED_PERIOD_ORDER(401, "PE02", "교시 순서 중복"),
  DUPLICATED_PERIOD_NAME(402, "PE03", "교시 이름 중복"),
  CROSSING_START_END_TIME(403, "PE04", "시작시간과 종료시간이 교차");

  private final int status;
  private final String code;
  private final String message;

  PeriodErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
