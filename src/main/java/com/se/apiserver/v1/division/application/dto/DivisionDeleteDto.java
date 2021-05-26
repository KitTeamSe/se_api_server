package com.se.apiserver.v1.division.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DivisionDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("분반 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 분반 ID", example = "1")
    private Long divisionId;
  }
}
