package com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityGroupAccountMappingCreateDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("그룹-사용자 매핑 추가 요청")
    static public class Request{
        @ApiModelProperty(notes = "사용자 아이디(pk)", example = "1")
        private Long accountId;

        @ApiModelProperty(notes = "권한 그룹 아이디(pk)", example = "1")
        private Long groupId;
    }
}
