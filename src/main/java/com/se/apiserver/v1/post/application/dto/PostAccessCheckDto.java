package com.se.apiserver.v1.post.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PostAccessCheckDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnonymousPostAccessCheckDto{

        private Long postId;
        private String password;
    }
}
