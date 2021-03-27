package com.se.apiserver.v1.tag.infra.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TagCreateDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("태그 등록 요청")
    static public class Request{
        @ApiModelProperty(notes = "태그", value = "학사")
        @Size(min = 2, max = 30)
        @NotEmpty
        private String text;
    }

    @Data
    @NoArgsConstructor
    @Builder
    static public class Response{

    }
}
