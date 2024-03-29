package com.se.apiserver.v1.post.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class PostDeleteDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel(value = "익명 게시글 삭제 요청")
    public static class AnonymousPostDeleteRequest{

        @ApiModelProperty(notes = "게시글 아이디", example = "1")
        private Long postId;

        @Size(min = 2, max = 255)
        private String anonymousPassword;
    }
}
