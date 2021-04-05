package com.se.apiserver.v1.teacher.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum TeacherErrorCode implements ErrorCode {
  NO_SUCH_TEACHER(400, "TE01", "존재하지 않는 교원"),
  DUPLICATED_TEACHER(401, "TE02", "교원 중복");

  private final int status;
  private final String code;
  private final String message;

  TeacherErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
