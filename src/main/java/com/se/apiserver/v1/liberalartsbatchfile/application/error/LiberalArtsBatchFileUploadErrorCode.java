package com.se.apiserver.v1.liberalartsbatchfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum LiberalArtsBatchFileUploadErrorCode implements ErrorCode {
  INVALID_FILE_NAME(400, "LABFUE01", "유효하지 않은 파일명"),
  INVALID_FILE_SIZE(400, "LABFUE02", "유효하지 않은 파일 크기"),
  FILE_SIZE_LIMIT_EXCEEDED(400, "LABFUE03", "파일 용량 초과"),
  UNKNOWN_UPLOAD_ERROR(400, "LABFUE04", "업로드 도중 알 수 없는 오류 발생");

  private final String code;
  private final String message;
  private int status;

  LiberalArtsBatchFileUploadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
