package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportResult;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import com.se.apiserver.v1.report.domain.entity.ReportType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportReadDto {

  @ApiModel("신고 조회 응답")
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private ReportStatus reportStatus;

    @ApiModelProperty(notes = "신고 처리 결과", example = "IP_BAN")
    private ReportResult reportResult;

    @ApiModelProperty(notes = "처리자 ID", example = "1")
    private Long processorId;

    @ApiModelProperty(notes = "신고자 ID", example = "1")
    private Long reporterId;

    @ApiModelProperty(notes = "생성일시")
    private LocalDateTime createdAt;
    @ApiModelProperty(notes = "수정일시")
    private LocalDateTime updatedAt;

    public Response(Long reportId, ReportType reportType, Long targetId, String description,
        ReportStatus reportStatus, ReportResult reportResult, Long processorId,
        Long reporterId, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.reportId = reportId;
      this.reportType = reportType;
      this.targetId = targetId;
      this.description = description;
      this.reportStatus = reportStatus;
      this.reportResult = reportResult;
      this.processorId = processorId;
      this.reporterId = reporterId;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }

    public static Response fromEntity(Report report){
      return Response.builder()
          .reportId(report.getReportId())
          .targetId(report.getTargetId())
          .reportType(report.getReportType())
          .description(report.getDescription())
          .reportStatus(report.getReportStatus())
          .reportResult(report.getReportResult())
          .processorId(report.getProcessor() == null ? null : report.getProcessor().getAccountId())
          .reporterId(report.getReporter().getAccountId())

          .createdAt(report.getCreatedAt())
          .updatedAt(report.getUpdatedAt())
          .build();
    }
  }

  @ApiModel("신고 검색 요청")
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  static public class ReportSearchRequest {

    @ApiModelProperty(notes = "검색할 신고 내용", example = "광고성 글이에요.")
    private String description;

    @ApiModelProperty(notes = "검색할 신고 상태", example = "SUBMITTED")
    private ReportStatus reportStatus;

    @ApiModelProperty(notes = "검색할 신고 처리 결과", example = "IP_BAN")
    private ReportResult reportResult;

    @ApiModelProperty(notes = "검색할 처리자 idString", example = "admin")
    private String processorIdString;

    @ApiModelProperty(notes = "검색할 신고자 idString", example = "user")
    private String reporterIdString;

    @NotNull
    private PageRequest pageRequest;

    public ReportSearchRequest(String description,
        ReportStatus reportStatus, ReportResult reportResult, String processorIdString,
        String reporterIdString, PageRequest pageRequest) {
      this.description = description;
      this.reportStatus = reportStatus;
      this.reportResult = reportResult;
      this.processorIdString = processorIdString;
      this.reporterIdString = reporterIdString;
      this.pageRequest = pageRequest;
    }
  }

}
