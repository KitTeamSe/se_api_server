package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileUploadErrorCode implements ErrorCode {

  UPLOAD_PATH_DOES_NOT_EXISTS(400, "MFUE01", "업로드할 경로가 존재하지 않음"),
  INVALID_FILE_NAME(400, "MFUE02", "유효하지 않은 파일명"),
  INVALID_FILE_SIZE(400, "MFUE03", "유효하지 않은 파일 크기"),
  UNKNOWN_UPLOAD_ERROR_CAUSED(400, "MFUE04", "업로드 도중 알 수 없는 오류 발생");

  private final String code;
  private final String message;
  private int status;

  MultipartFileUploadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
