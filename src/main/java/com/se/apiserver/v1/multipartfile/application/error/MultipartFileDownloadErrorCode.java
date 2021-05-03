package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileDownloadErrorCode implements ErrorCode {

  DOWNLOAD_PATH_DOES_NOT_EXISTS(400, "MFDE01", "파일을 다운로드 할 경로가 존재하지 않음"),
  FILE_DOES_NOT_EXISTS(400, "MFDE02", "파일을 찾지 못함"),
  COULD_NOT_DETERMINE_FILE_TYPE(400, "MFDE03", "파일 타입을 결정하지 못함"),
  INTERNAL_FILE_SERVER_ERROR(400, "MFDE04", "내부 파일 서버에서 오류 발생"),
  UNKNOWN_DOWNLOAD_ERROR(400, "MFDE04", "다운로드 도중 알 수 없는 에러 발생");

  private final String code;
  private final String message;
  private int status;

  MultipartFileDownloadErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
