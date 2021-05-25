package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReportUpdateDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("신고 수정 요청")
  static public class Request{

    @ApiModelProperty(notes = "신고 id", example = "1")
    private Long reportId;

    @ApiModelProperty(notes = "변경할 설명", example = "변경된 설명")
    private String description;

    @ApiModelProperty(notes = "변경할 상태", example = "ACCEPTED")
    private ReportStatus reportStatus;
  }
}
