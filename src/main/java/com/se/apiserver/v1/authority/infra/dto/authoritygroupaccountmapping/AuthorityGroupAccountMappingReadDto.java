package com.se.apiserver.v1.authority.infra.dto.authoritygroupaccountmapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAccountMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityGroupAccountMappingReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long id;

        private Long accountId;

        private String accountIdString;

        private Long groupId;

        private String groupName;

        public static Response fromEntity(AuthorityGroupAccountMapping authorityGroupAccountMapping){
            return Response.builder()
                    .id(authorityGroupAccountMapping.getAuthorityGroupAccountMappingId())
                    .accountId(authorityGroupAccountMapping.getAccount().getAccountId())
                    .accountIdString(authorityGroupAccountMapping.getAccount().getIdString())
                    .groupId(authorityGroupAccountMapping.getAuthorityGroup().getAuthorityGroupId())
                    .groupName(authorityGroupAccountMapping.getAuthorityGroup().getName())
                    .build();
        }
    }
}
