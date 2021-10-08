package com.se.apiserver.v1.post.application.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostAccessCheckDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "익명 게시글 수정(쓰기) 권한 확인 요청")
    public static class AnonymousPostAccessCheckDto{
        private Long postId;
        private String password;
    }
}
