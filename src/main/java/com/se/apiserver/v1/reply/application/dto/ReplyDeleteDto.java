package com.se.apiserver.v1.reply.application.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReplyDeleteDto {
    @ApiModel(value = "익명 댓글 삭제 요청")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class AnonymousReplyDeleteRequest{
        private Long replyId;
        private String password;
    }
}
