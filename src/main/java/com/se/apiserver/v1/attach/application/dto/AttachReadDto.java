package com.se.apiserver.v1.attach.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AttachReadDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  static public class Response {

    private Long attachId;

    @JsonInclude(Include.NON_NULL)
    private Long postId;

    @JsonInclude(Include.NON_NULL)
    private Long replyId;

    private String downloadUrl;

    private String fileName;

    public static Response fromEntity(Attach attach) {
      ResponseBuilder responseBuilder = Response.builder()
          .attachId(attach.getAttachId())
          .downloadUrl(attach.getDownloadUrl())
          .fileName(attach.getFileName());

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
