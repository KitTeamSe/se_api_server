package com.se.apiserver.v1.authority.infra.dto.authority;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.menu.domain.entity.Menu;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorityReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel
    public static class Response{
        private Long authorityId;

        private String nameEng;

        private String nameKor;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String menuNameKor;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String menuNameEng;

        public static Response fromEntity(Authority authority) {
            ResponseBuilder responseBuilder = Response.builder()
                    .authorityId(authority.getAuthorityId())
                    .nameEng(authority.getNameEng())
                    .nameKor(authority.getNameKor());
            if(authority.getMenu() != null) {
                responseBuilder.menuNameKor(authority.getMenu().getNameKor());
                responseBuilder.menuNameEng(authority.getMenu().getNameEng());
            }
            return responseBuilder.build();
        }
    }
}
