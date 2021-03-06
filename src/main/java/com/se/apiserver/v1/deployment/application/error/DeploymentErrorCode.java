package com.se.apiserver.v1.deployment.application.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeploymentErrorCode implements ErrorCode {

  NO_SUCH_DEPLOYMENT(400, "DE01", "존재하지 않는 배치"),
  EMPTY_NUMBER_OF_DIVISION(400, "DE02", "분반 수가 입력되지 않았음"),
  OVER_TEACHING_PER_WEEK(400, "DE03", "주간 수업 시간 초과"),
  TEACHER_LECTURE_UNABLE_TIME(400, "DE04", "해당 교원의 강의 불가 시간"),
  TEACHER_LECTURES_SAME_TIME(400, "DE05", "해당 교원은 해당 시간에 배치된 정보가 존재"),
  SAME_GRADE_LECTURED(400, "DE06", "해당 학년은 동 시간대에 수업 중"),
  DEPLOYMENT_OVERLAPPED(400, "DE07", "해당 요일, 해당 시간, 해당 강의실에 겹치는 배치 정보 존재");

  private final int status;
  private final String code;
  private final String message;

  DeploymentErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
