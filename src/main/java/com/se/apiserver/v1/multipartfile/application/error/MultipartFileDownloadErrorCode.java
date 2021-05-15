package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileDownloadErrorCode implements ErrorCode {
  FAILED_TO_CONNECT_FILE_SERVER(400, "MFDLE01", "파일 서버에 접속할 수 없음"),
  UNKNOWN_FILE_DOWNLOAD_ERROR(400, "MFDLE02", "파일 다운로드 중 알 수 없는 에러 발생");

  private final String code;
  private final String message;
  private int status;

  MultipartFileDownloadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
