package com.se.apiserver.v1.period.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum  PeriodRangeError implements ErrorCode {

  INVALID_PERIOD_RANGE(400, "PRE01", "유효하지 않은 교시 범위");

  private final int status;
  private final String code;
  private final String message;

  PeriodRangeError(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
