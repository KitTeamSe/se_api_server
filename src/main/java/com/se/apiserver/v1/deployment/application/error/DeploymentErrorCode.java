package com.se.apiserver.v1.deployment.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum DeploymentErrorCode implements ErrorCode {

  NO_SUCH_DEPLOYMENT(400, "DE01", "존재하지 않는 배치"),
  INVALID_DIVISION(401, "DE02", "유효하지 않은 분반");

  private final int status;
  private final String code;
  private final String message;

  DeploymentErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
