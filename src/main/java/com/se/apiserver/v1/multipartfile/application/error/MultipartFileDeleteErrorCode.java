package com.se.apiserver.v1.multipartfile.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum MultipartFileDeleteErrorCode implements ErrorCode {
  FAILED_TO_CONNECT_FILE_SERVER(400, "MFDE01", "파일 서버에 접속할 수 없음"),
  UNKNOWN_FILE_DELETE_ERROR(400, "MFDE02", "파일 삭제 중 알 수 없는 오류 발생"),
  FAILED_TO_PARSE_RESPONSE(400, "MFDE03", "파일 서버 응답 파싱 실패"),
  EMPTY_DOWNLOAD_URL(400, "MFDE04", "유효하지 않은 다운로드 링크");


  private final String code;
  private final String message;
  private int status;

  MultipartFileDeleteErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
