package com.se.apiserver.v1.authority.application.dto.authority;


import com.se.apiserver.v1.authority.domain.entity.Authority;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("권한 조회 응답")
    public static class Response{
        private Long authorityId;

        private String nameEng;

        private String nameKor;

        public static Response fromEntity(Authority authority) {
            ResponseBuilder responseBuilder = Response.builder()
                    .authorityId(authority.getAuthorityId())
                    .nameEng(authority.getNameEng())
                    .nameKor(authority.getNameKor());
            return responseBuilder.build();
        }
    }
}
