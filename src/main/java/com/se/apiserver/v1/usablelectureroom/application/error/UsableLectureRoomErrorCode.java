package com.se.apiserver.v1.usablelectureroom.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum UsableLectureRoomErrorCode implements ErrorCode {

  NO_SUCH_USABLE_LECTURE_ROOM(400, "ULRE01", "존재하지 않는 사용 가능 강의실"),
  DUPLICATED_USABLE_LECTURE_ROOM(400, "ULRE02", "해당 강의실은 이미 해당 시간표에 사용중");

  private final int status;
  private final String code;
  private final String message;

  UsableLectureRoomErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
