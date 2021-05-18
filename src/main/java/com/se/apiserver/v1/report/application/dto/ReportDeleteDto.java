package com.se.apiserver.v1.report.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReportDeleteDto {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @ApiModel("신고 삭제 요청")
  static public class Request{
    @ApiModelProperty(notes = "삭제할 신고 번호", example = "1")
    private Long reportId;
  }
}
