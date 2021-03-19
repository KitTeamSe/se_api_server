package com.se.apiserver.http.dto.account;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountSignInDto {
    @Data
    @AllArgsConstructor
    @ApiModel("로그인 응답")
    static public class Response {
        private String token;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("로그인 요청")
    static public class Request {
        private String id;
        private String pw;
    }
}
