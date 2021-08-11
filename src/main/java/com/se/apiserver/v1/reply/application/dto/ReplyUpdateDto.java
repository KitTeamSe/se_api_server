package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReplyUpdateDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel(value = "댓글 수정 요청")
    public static class Request{
        @NotNull
        @Min(1)
        @ApiModelProperty(notes = "댓글 아이디", example = "1")
        private Long replyId;

        @ApiModelProperty(notes = "익명 댓글 비밀번호", example = "string")
        private String password;

        @NotNull
        @Min(1)
        @ApiModelProperty(notes = "게시글 아이디", example = "1")
        private Long postId;

        @NotNull
        @ApiModelProperty(notes = "댓글 내용", example = "string")
        private String text;

        @ApiModelProperty(notes = "비밀 여부")
        @NotNull
        private ReplyIsSecret isSecret;

        @ApiModelProperty(notes = "첨부파일들")
        @Singular("attaches")
        private List<Long> attachIdList = new ArrayList<>();
    }
}
