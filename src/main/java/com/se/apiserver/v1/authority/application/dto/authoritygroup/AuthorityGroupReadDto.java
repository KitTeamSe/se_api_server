package com.se.apiserver.v1.authority.application.dto.authoritygroup;

import com.se.apiserver.v1.authority.application.dto.authority.AuthorityReadDto;
import com.se.apiserver.v1.authority.domain.entity.Authority;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthorityGroupReadDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class AuthorityGroupListItem{
        private Long authorityGroupId;

        private String name;

        private String description;

        private AuthorityGroupType type;

        public static AuthorityGroupListItem fromEntity(AuthorityGroup authorityGroup){
            return AuthorityGroupListItem.builder()
                .authorityGroupId(authorityGroup.getAuthorityGroupId())
                .name(authorityGroup.getName())
                .description(authorityGroup.getDescription())
                .type(authorityGroup.getType())
                .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long authorityGroupId;

        private String name;

        private String description;

        private AuthorityGroupType type;

        private List<AuthorityReadDto.Response> authorities;

        public static Response fromEntity(AuthorityGroup authorityGroup, List<Authority> authorities){
            Response.ResponseBuilder builder = Response.builder();

            builder.authorities(authorities.stream().map(
                AuthorityReadDto.Response::fromEntity).collect(
                Collectors.toList()));

            return builder
                .authorityGroupId(authorityGroup.getAuthorityGroupId())
                .name(authorityGroup.getName())
                .description(authorityGroup.getDescription())
                .type(authorityGroup.getType())
                .build();
        }
    }
}
