package com.se.apiserver.v1.reply.application.dto;

import com.se.apiserver.v1.account.domain.entity.Account;
import com.se.apiserver.v1.attach.domain.entity.Attach;
import com.se.apiserver.v1.common.domain.entity.Anonymous;
import com.se.apiserver.v1.post.domain.entity.Post;
import com.se.apiserver.v1.reply.domain.entity.Reply;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsDelete;
import com.se.apiserver.v1.reply.domain.entity.ReplyIsSecret;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ReplyCreateDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel(value = "댓글 생성 요청")
    public static class Request{
        @NotNull
        @Min(1)
        @ApiModelProperty(notes = "게시글 아이디", example = "1")
        private Long postId;

        @NotNull
        @ApiModelProperty(notes = "댓글 내용", example = "string")
        private String text;

        @ApiModelProperty(notes = "익명 사용자 정보, 회원으로 등록일 경우 생략")
        private Anonymous anonymous;

        @Min(1)
        @ApiModelProperty(notes = "부모 댓글의 번호")
        private Long parentId;

        @ApiModelProperty(notes = "비밀 여부")
        @NotNull
        private ReplyIsSecret isSecret;

        @ApiModelProperty(notes = "첨부파일들")
        @Singular("attachmentList")
        private List<AttachDto> attaches = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttachDto{
        private Long attachId;
    }
}


