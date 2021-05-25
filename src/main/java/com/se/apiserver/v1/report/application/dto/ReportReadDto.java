package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.domain.entity.ReportType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReportReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @ApiModel("신고 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "신고 ID", example = "1")
    private Long reportId;

    @ApiModelProperty(notes = "신고 대상 타입", example = "POST")
    private ReportType reportType;

    @ApiModelProperty(notes = "대상 ID", example = "1")
    private Long targetId;

    @ApiModelProperty(notes = "신고 내용", example = "광고성 글이에요.")
    private String description;

    @ApiModelProperty(notes = "신고 상태", example = "SUBMITTED")
    private ReportStatus status;

    @ApiModelProperty(notes = "처리자 닉네임", example = "admin")
    private String processorNickname;

    @ApiModelProperty(notes = "신고자 닉네임", example = "advertiser")
    private String reporterNickname;

    public static Response fromEntity(Report report){
      return Response.builder()
          .reportId(report.getReportId())
          .targetId(report.getTargetId())
          .reportType(report.getReportType())
          .description(report.getDescription())
          .status(report.getReportStatus())
          .processorNickname(report.getProcessor() == null ? null : report.getProcessor().getNickname())
          .reporterNickname(report.getReporter().getNickname())
          .build();
    }
  }


}
