package com.se.apiserver.v1.timetable.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum TimeTableErrorCode implements ErrorCode {

  NO_SUCH_TIME_TABLE(400, "TTE01", "존재하지 않는 시간표"),
  DUPLICATED_TIME_TABLE_NAME(400, "TTE02", "시간표 이름 중복"),
  INVALID_YEAR(400, "TTE03", "유효하지 않은 년도"),
  INVALID_SEMESTER(400, "TTE04", "유효하지 않은 학기");

  private final int status;
  private final String code;
  private final String message;

  TimeTableErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
