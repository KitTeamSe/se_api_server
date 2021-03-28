package com.se.apiserver.v1.authority.infra.dto.authoritygroup;

import com.se.apiserver.v1.authority.domain.entity.AuthorityGroupType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthorityGroupUpdateDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("권한 그룹 수정")
    static public class Request{

        @NotNull
        @ApiModelProperty(notes = "권한 그룹 아이디", example = "1")
        private Long id;

        @ApiModelProperty(notes = "권한 그룹명", example = "사용자")
        @Size(min = 2, max = 30)
        private String name;

        @ApiModelProperty(notes = "권한 그룹명", example = "사용자 그룹")
        @Size(min = 2, max = 100)
        private String description;

    }
}
