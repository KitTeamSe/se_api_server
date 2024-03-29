package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

public class ReplyUpdateDto {
    @Getter
    @NoArgsConstructor
    @Builder
    @ApiModel(value = "댓글 수정 요청")
    public static class Request{

        public Request(Long replyId, String password, String text,
            ReplyIsSecret isSecret,
            List<ReplyUpdateAttachDto> attachmentList) {
            this.replyId = replyId;
            this.password = password;
            this.text = text;
            this.isSecret = isSecret;
            this.attachmentList = attachmentList;
        }

        @NotNull
        @Min(1)
        @ApiModelProperty(notes = "댓글 아이디", example = "1")
        private Long replyId;

        @ApiModelProperty(notes = "익명 댓글 비밀번호", example = "string")
        private String password;

        @NotNull
        @ApiModelProperty(notes = "댓글 내용", example = "string")
        private String text;

        @ApiModelProperty(notes = "비밀 여부")
        @NotNull
        private ReplyIsSecret isSecret;

        @ApiModelProperty(notes = "첨부파일들")
        @Singular("attachmentList")
        private List<ReplyUpdateAttachDto> attachmentList;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    public static class ReplyUpdateAttachDto {
        private Long attachId;

        public ReplyUpdateAttachDto(Long attachId) {
            this.attachId = attachId;
        }
    }
}
