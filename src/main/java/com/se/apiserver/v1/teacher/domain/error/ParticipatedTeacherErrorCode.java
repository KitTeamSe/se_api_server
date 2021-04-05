package com.se.apiserver.v1.teacher.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ParticipatedTeacherErrorCode implements ErrorCode {
  NO_SUCH_PARTICIPATED_TEACHER(400, "PTE01", "존재하지 않는 참여 교원"),
  DUPLICATED_PARTICIPATED_TEACHER(401, "PTE02", "참여 교원 중복");

  private final int status;
  private final String code;
  private final String message;

  ParticipatedTeacherErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
