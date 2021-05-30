package com.se.apiserver.v1.liberalartsbatch.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum LiberalArtsBatchParseErrorCode implements ErrorCode {
  INVALID_EXCEL_DATA(400, "LABPRE01", "엑셀 내 유효하지 않은 데이터 존재"),
  INVALID_PERIOD_DATA(400, "LABPRE02", "엑셀 내 유효하지 않은 교시 존재"),
  INVALID_DIVISION_NUMBER_DATA(400, "LABPRE03", "엑셀 내 유효하지 않은 분반 정보 존재"),
  INVALID_LECTURE_ROOM_DATA(400, "LABPRE04", "엑셀 내 유효하지 않은 강의실 정보 존재"),
  INVALID_SUBJECT_CODE_DATA(400, "LABPRE05", "엑셀 내 유효하지 않은 교과 정보 존재"),
  DUPLICATED_SUBJECT_CODE(400, "LABPRE06", "교과목 코드는 동일하지만, 이름이 다른 교과가 존재");

  private final String code;
  private final String message;
  private int status;

  LiberalArtsBatchParseErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }
}
