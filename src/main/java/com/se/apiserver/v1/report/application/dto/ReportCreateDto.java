package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.domain.entity.ReportType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReportCreateDto {

  @Data
  @NoArgsConstructor
  @Builder
  @AllArgsConstructor
  @ApiModel("신고 생성 요청")
  static public class Request{

    @ApiModelProperty(notes = "대상 유형(POST/REPLY)", example = "POST")
    private ReportType reportType;

    @ApiModelProperty(notes = "대상 ID(pk)", example = "1")
    private Long targetId;

    @ApiModelProperty(notes = "신고 내용", example = "광고 같아요")
    @Size(min = 2, max = 255)
    private String description;
  }
}
