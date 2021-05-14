package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.ReportStatus;
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

    @ApiModelProperty(notes = "게시글 ID", example = "1")
    private Long postId;

    @ApiModelProperty(notes = "댓글 ID (없으면 생략)", example = "1")
    private Long replyId;

    @ApiModelProperty(notes = "신고 내용", example = "광고 같아요")
    @Size(min = 2, max = 255)
    private String text;
  }
}
