package com.se.apiserver.v1.period.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalTime;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PeriodCreateDto {
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("교시 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "순서", example = "1")
    private Integer periodOrder;

    @ApiModelProperty(notes = "이름(1~9,A,B,C,D...)", example = "1")
    @Size(min = 1, max = 20)
    private String name;

    @ApiModelProperty(notes = "시작시간", example = "09:00", dataType = "java.lang.String")
    private LocalTime startTime;

    @ApiModelProperty(notes = "종료시간", example = "09:50", dataType = "java.lang.String")
    private LocalTime endTime;

    @ApiModelProperty(notes = "비고", example = "오전 9시부터 오전 10시까지 하는 수업")
    @Size(max = 255)
    private String note;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("교시 생성 응답")
  static public class Response{
    @ApiModelProperty(notes = "교시 pk", example = "1")
    private Long periodId;
  }
}
