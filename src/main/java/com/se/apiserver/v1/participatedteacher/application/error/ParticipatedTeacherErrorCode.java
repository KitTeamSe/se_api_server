package com.se.apiserver.v1.participatedteacher.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ParticipatedTeacherErrorCode implements ErrorCode {
  NO_SUCH_PARTICIPATED_TEACHER(400, "PTE01", "존재하지 않는 참여 교원"),
  DUPLICATED_PARTICIPATED_TEACHER(400, "PTE02", "해당 교원은 이미 해당 시간표에 참여중");

  private final int status;
  private final String code;
  private final String message;

  ParticipatedTeacherErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
