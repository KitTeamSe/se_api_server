package com.se.apiserver.v1.usablelectureroom.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsableLectureRoomDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("사용 가능 강의실 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 사용 가능 강의실 id", example = "1")
    private Long usableLectureRoomId;
  }
}
