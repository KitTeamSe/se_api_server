package com.se.apiserver.v1.authority.infra.dto.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroup;
import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
