package com.se.apiserver.v1.usablelectureroom.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UsableLectureRoomCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("사용 가능 강의실 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 번호", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "강의실 번호", example = "1")
    private Long lectureRoomId;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("사용 가능 강의실 생성 응답")
  static public class Resposne{
    @ApiModelProperty(notes = "사용 가능 강의실 pk", example = "1")
    private Long usableLectureRoomId;
  }
}
