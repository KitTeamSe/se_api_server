package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.ReportType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportCreateDto {


  @ApiModel("신고 생성 요청")
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  static public class Request{

    @ApiModelProperty(notes = "대상 유형(POST/REPLY)", example = "POST")
    private ReportType reportType;

    @ApiModelProperty(notes = "대상 ID(pk)", example = "1")
    private Long targetId;

    @ApiModelProperty(notes = "신고 내용", example = "광고 같아요")
    @Size(min = 5, max = 255)
    private String description;

    public Request(ReportType reportType, Long targetId, String description) {
      this.reportType = reportType;
      this.targetId = targetId;
      this.description = description;
    }

  }
}
