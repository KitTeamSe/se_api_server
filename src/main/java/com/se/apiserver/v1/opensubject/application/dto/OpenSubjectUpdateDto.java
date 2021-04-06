package com.se.apiserver.v1.opensubject.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OpenSubjectUpdateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("개설 교과 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "개설 교과 번호", example = "1")
    private Long openSubjectId;

    @ApiModelProperty(notes = "분반 수", example = "1")
    private Integer numberOfDivision;

    @ApiModelProperty(notes = "주간 수업 시간", example = "1", allowEmptyValue = true)
    private Integer teachingTimePerWeek;

  }
}
