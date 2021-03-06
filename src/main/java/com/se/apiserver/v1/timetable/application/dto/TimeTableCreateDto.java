package com.se.apiserver.v1.timetable.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TimeTableCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("시간표 생성 요청")
  static public class Request{
    @ApiModelProperty(notes = "이름", example = "테스트 시간표 1")
    @Size(min = 1, max = 50)
    private String name;

    @ApiModelProperty(notes = "년도", example = "2021")
    private Integer year;

    @ApiModelProperty(notes = "학기", example = "1")
    private Integer semester;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("시간표 생성 응답")
  static public class Response{
    @ApiModelProperty(notes = "시간표 pk", example = "1")
    private Long timeTableId;
  }
}
