package com.se.apiserver.v1.division.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DivisionUpdateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("분반 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "변경할 분반 ID", example = "1")
    private Long divisionId;

    @ApiModelProperty(notes = "변경할 분반 번호", example = "1")
    private Integer divisionNumber;

    @ApiModelProperty(notes = "변경할 비고", example = "변경된 비고")
    private String note;
  }
}
