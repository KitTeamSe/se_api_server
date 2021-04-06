package com.se.apiserver.v1.lectureunabletime.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum  LectureUnableTimeErrorCode implements ErrorCode {

  NO_SUCH_LECTURE_UNABLE_TIME(400, "LUTE01", "존재하지 않는 강의 불가 시간"),
  CROSSING_START_END_PERIOD(401, "LUTE02", "시작교시외 종료교시가 교차");

  private final int status;
  private final String code;
  private final String message;

  LectureUnableTimeErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
