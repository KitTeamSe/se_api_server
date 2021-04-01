package com.se.apiserver.v1.timetable.infra.dto;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
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
    @Size(min = 2, max = 20)
    private String name;

    @ApiModelProperty(notes = "년도", example = "2021")
    private Integer year;

    @ApiModelProperty(notes = "학기", example = "1")
    private Integer semester;

//    @ApiModelProperty(notes = "생성한 사용자", example = "1")
//    private Account creatorAccount;

    @ApiModelProperty(notes = "상태", example = "CREATION")
    private TimeTableStatus status;
  }

  @Data
  @AllArgsConstructor
  @ApiModel("시간표 생성 응답")
  static public class Response{
    @ApiModelProperty(notes = "시간표 pk", example = "1")
    private Long timeTableId;
  }
}
