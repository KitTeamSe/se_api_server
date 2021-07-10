package com.se.apiserver.v1.timetable.application.dto;

import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TimeTableUpdateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("시간표 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "변경할 이름", example = "테스트 시간표 1")
    private String name;

    @ApiModelProperty(notes = "변경할 년도", example = "2021")
    private Integer year;

    @ApiModelProperty(notes = "변경할 학기", example = "2")
    private Integer semester;

    @ApiModelProperty(notes = "변경할 상태", example = "CREATED")
    private TimeTableStatus status;
  }
}
