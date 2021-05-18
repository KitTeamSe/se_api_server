package com.se.apiserver.v1.report.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ReportErrorCode implements ErrorCode {
  NO_SUCH_REPORT(400, "REC01", "존재하지 않는 신고"),
  INVALID_INPUT(400, "REC02", "입력 값이 올바르지 않음"),
  NO_SUCH_REPORT_TYPE(400, "REC03", "존재하지 않는 신고 유형"),
  ANONYMOUS_CANT_REPORT(400, "REC04", "익명 사용자는 신고할 수 없음");

  private final int status;
  private final String code;
  private final String message;

  ReportErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
