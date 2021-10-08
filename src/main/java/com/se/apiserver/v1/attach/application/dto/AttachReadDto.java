package com.se.apiserver.v1.attach.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AttachReadDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @ApiModel("첨부 파일 조회 응답")
  @Builder
  static public class Response {

    private Long attachId;

    @JsonInclude(Include.NON_NULL)
    private Long postId;

    @JsonInclude(Include.NON_NULL)
    private Long replyId;

    private String downloadUrl;

    private String fileName;

    private Long fileSize;

    public static Response fromEntity(Attach attach) {
      ResponseBuilder responseBuilder = Response.builder()
          .attachId(attach.getAttachId())
          .downloadUrl(attach.getDownloadUrl())
          .fileName(attach.getFileName())
          .fileSize(attach.getFileSize());

      if (attach.getPost() != null) {
        responseBuilder.postId(attach.getPost().getPostId());
      }

      if (attach.getReply() != null) {
        responseBuilder.replyId(attach.getReply().getReplyId());
      }

      return responseBuilder.build();
    }
  }
}
