package com.se.apiserver.v1.authority.application.dto.authoritygroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class AuthorityGroupCreateDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("권한 그룹 등록")
    static public class Request{
        @ApiModelProperty(notes = "권한 그룹명", example = "사용자")
        @Size(min = 2, max = 30)
        private String name;

        @ApiModelProperty(notes = "권한 그룹명", example = "사용자 그룹")
        @Size(min = 2, max = 100)
        private String description;
    }
}
