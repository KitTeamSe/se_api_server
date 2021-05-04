package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileDownloadErrorCode implements ErrorCode {
  INTERNAL_FILE_SERVER_ERROR(400, "MFDE01", "내부 파일 서버에서 오류 발생"),
  UNKNOWN_DOWNLOAD_ERROR(400, "MFDE02", "다운로드 도중 알 수 없는 에러 발생");

  private final String code;
  private final String message;
  private int status;

  MultipartFileDownloadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
