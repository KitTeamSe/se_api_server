package com.se.apiserver.v1.timetable.infra.dto;

import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TimeTableReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("시간표 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "시간표 id", example = "1")
    private Long timeTableId;

    @ApiModelProperty(notes = "이름", example = "테스트 시간표 1")
    private String name;

    @ApiModelProperty(notes = "년도", example = "2021")
    private Integer year;

    @ApiModelProperty(notes = "학기", example = "2")
    private Integer semester;

    @ApiModelProperty(notes = "작성자 id", example = "1")
    private Long createdBy;

    @ApiModelProperty(notes = "상태", example = "CREATED")
    private TimeTableStatus status;

    public static Response fromEntity(TimeTable timeTable){
      return Response.builder()
          .timeTableId(timeTable.getTimeTableId())
          .name(timeTable.getName())
          .year(timeTable.getYear())
          .semester(timeTable.getSemester())
          .createdBy(timeTable.getCreatedBy())
          .status(timeTable.getStatus())
          .build();
    }
  }
}
