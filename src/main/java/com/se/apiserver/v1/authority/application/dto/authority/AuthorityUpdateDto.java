package com.se.apiserver.v1.authority.application.dto.authority;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

public class AuthorityUpdateDto {

    @Builder
    @Data
    @ApiModel("권한 수정 요청")
    static public class Request{

        private Long id;

        @Size(min = 2, max = 20)
        private String nameEng;

        @Size(min = 2, max = 20)
        private String nameKor;
    }
}
