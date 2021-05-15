package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileUploadErrorCode implements ErrorCode {
  FAILED_TO_CONNECT_FILE_SERVER(400, "MFULE01", "파일 서버에 접속할 수 없음"),
  UNKNOWN_FILE_UPLOAD_ERROR(400, "MFULE02", "파일 업로드 중 알 수 없는 오류 발생"),
  INVALID_FILE_SIZE(400, "MFULE03", "유효하지 않은 파일 크기"),
  FILE_SIZE_LIMIT_EXCEEDED(400, "MFULE04", "파일 용량 초과");


  private final String code;
  private final String message;
  private int status;

  MultipartFileUploadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
