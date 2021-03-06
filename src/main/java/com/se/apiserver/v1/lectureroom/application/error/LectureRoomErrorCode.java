package com.se.apiserver.v1.lectureroom.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum LectureRoomErrorCode implements ErrorCode {

  NO_SUCH_LECTURE_ROOM(400, "LRE01", "존재하지 않는 강의실"),
  DUPLICATED_LECTURE_ROOM(400, "LRE02", "강의실 중복"),
  INVALID_CAPACITY(400, "LRE03", "유효하지 않은 정원");

  private final int status;
  private final String code;
  private final String message;

  LectureRoomErrorCode(int status, String code, String message){
    this.status = status;
    this.code = code;
    this.message = message;
  }

}
