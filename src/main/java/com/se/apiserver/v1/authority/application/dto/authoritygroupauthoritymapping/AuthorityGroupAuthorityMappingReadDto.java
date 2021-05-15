package com.se.apiserver.v1.authority.application.dto.authoritygroupauthoritymapping;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupAuthorityMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityGroupAuthorityMappingReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long id;

        private Long authorityId;

        private String authorityIdNameKor;

        private String authorityIdNameEng;

        private Long groupId;

        private String groupName;

        public static Response fromEntity(AuthorityGroupAuthorityMapping authorityGroupAuthorityMapping){
            return Response.builder()
                    .id(authorityGroupAuthorityMapping.getAuthorityGroupAuthorityMappingId())
                    .authorityId(authorityGroupAuthorityMapping.getAuthority().getAuthorityId())
                    .authorityIdNameKor(authorityGroupAuthorityMapping.getAuthority().getNameKor())
                    .authorityIdNameEng(authorityGroupAuthorityMapping.getAuthority().getNameEng())
                    .groupId(authorityGroupAuthorityMapping.getAuthorityGroup().getAuthorityGroupId())
                    .groupName(authorityGroupAuthorityMapping.getAuthorityGroup().getName())
                    .build();
        }
    }
}
