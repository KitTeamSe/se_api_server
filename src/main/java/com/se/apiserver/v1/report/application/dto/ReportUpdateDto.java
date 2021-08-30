package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.ReportResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportUpdateDto {

  @ApiModel("신고 처리 요청")
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  static public class Request{

    @ApiModelProperty(notes = "신고 id", example = "1")
    @NotNull
    private Long reportId;

    @ApiModelProperty(notes = "변경할 결과", example = "TARGET_DELETE")
    @NotNull
    private ReportResult reportResult;

    public Request(Long reportId, ReportResult reportResult) {
      this.reportId = reportId;
      this.reportResult = reportResult;
    }

  }
}
