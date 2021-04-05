package com.se.apiserver.v1.authority.application.dto.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityGroupReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long authorityGroupId;

        private String name;

        private String description;

        private AuthorityGroupType type;

        public static Response fromEntity(AuthorityGroup authorityGroup){
            return Response.builder()
                    .authorityGroupId(authorityGroup.getAuthorityGroupId())
                    .name(authorityGroup.getName())
                    .description(authorityGroup.getDescription())
                    .type(authorityGroup.getType())
                    .build();
        }
    }
}
