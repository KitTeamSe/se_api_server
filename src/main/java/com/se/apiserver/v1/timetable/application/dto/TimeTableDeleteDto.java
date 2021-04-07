package com.se.apiserver.v1.timetable.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TimeTableDeleteDto {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("시간표 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 시간표 번호", example = "1")
    private Long timeTableId;
  }
}
