package com.se.apiserver.v1.division.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DivisionCreateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("분반 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "개설 교과 번호", example = "1")
    private Long openSubjectId;
  }
}
