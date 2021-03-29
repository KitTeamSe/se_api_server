package com.se.apiserver.v1.tag.infra.dto.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TagUpdateDto {
    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @ApiModel("태그 수정 요청")
    static public class Request{
        @ApiModelProperty(notes = "태그 아이디", value = "1")
        private Long tagId;
        @ApiModelProperty(notes = "태그", value = "학사")
        @Size(min = 2, max = 30)
        @NotEmpty
        private String text;
    }
}
