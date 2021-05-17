package com.se.apiserver.v1.report.application.dto;

import com.se.apiserver.v1.report.domain.entity.Report;
import com.se.apiserver.v1.report.domain.entity.ReportStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReportReadDto {

  @Data
  @NoArgsConstructor
  @Builder
  @ApiModel("신고 조회 응답")
  static public class Response{

    @ApiModelProperty(notes = "신고 ID", example = "1")
    private Long reportId;

    @ApiModelProperty(notes = "게시글 ID", example = "1")
    private Long postId;

    @ApiModelProperty(notes = "댓글 ID", example = "1")
    private Long replyId;

    @ApiModelProperty(notes = "신고 내용", example = "1")
    private String text;

    @ApiModelProperty(notes = "신고 상태", example = "1")
    private ReportStatus status;

    @ApiModelProperty(notes = "처리자", example = "1")
    private String processorNickname;

    @ApiModelProperty(notes = "신고자", example = "1")
    private String reporterNickname;

    @ApiModelProperty(notes = "신고 대상", example = "1")
    private String reportedNickname;

    public static Response fromEntity(Report report){
      return Response.builder()
          .reportId(report.getReportId())
          .postId(report.getPost().getPostId())
          .replyId(report.getReply().getReplyId())
          .text(report.getText())
          .status(report.getStatus())
          .processorNickname(report.getProcessor().getNickname())
          .reporterNickname(report.getReporter().getNickname())
          .reportedNickname(report.getReported().getNickname())
          .build();
    }
  }


}
