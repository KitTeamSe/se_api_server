package com.se.apiserver.v1.attach.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AttachCreateDto {
  @Data
  @ApiModel("첨부파일 생성 요청")
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class Request{
    @ApiModelProperty(notes = "파일 명", example = "testFile")
    @Size(min = 2, max = 255)
    private String fileName;

    @ApiModelProperty(notes = "다운로드 url", example = "test/test")
    @Size(min = 2, max = 255)
    private String downloadUrl;

    @ApiModelProperty(notes = "게시글 아이디, 없으면 생략", example = "1")
    private Long postId;


    @ApiModelProperty(notes = "댓글 아이디, 없으면 생략", example = "1")
    private Long replyId;
  }
}
