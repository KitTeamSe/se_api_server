package com.se.apiserver.v1.lectureroom.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureRoomDeleteDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("강의실 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 강의실 id", example = "1")
    private Long lectureRoomId;
  }
}
