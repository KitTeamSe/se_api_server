package com.se.apiserver.v1.authority.infra.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

public class AuthorityUpdateDto {

    @Builder
    @Data
    static public class Request{

        private Long id;

        @Size(min = 2, max = 20)
        private String nameEng;

        @Size(min = 2, max = 20)
        private String nameKor;
    }
}