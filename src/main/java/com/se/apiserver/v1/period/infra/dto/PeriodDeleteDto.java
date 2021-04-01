package com.se.apiserver.v1.period.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PeriodDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("교시 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 교시 id", example = "1")
    private Long periodId;
  }
}
