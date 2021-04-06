package com.se.apiserver.v1.lectureunabletime.application.dto;

import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LectureUnableTimeUpdateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("강의 불가 시간 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "강의 불가 시간 번호", example = "1")
    private Long lectureUnableTimeId;

    @ApiModelProperty(notes = "요일(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY)", example = "MONDAY")
    private DayOfWeek dayOfWeek;

    @ApiModelProperty(notes = "시작 교시 번호", example = "1")
    private Long startPeriodId;

    @ApiModelProperty(notes = "종료 교시 번호", example = "1")
    private Long endPeriodId;

    @ApiModelProperty(notes = "비고", example = "1번 참여 교원은 1교시부터 1교시까지 강의 불가합니다.")
    @Size(max = 255)
    private String note;

  }
}
