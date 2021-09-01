package com.se.apiserver.v1.tag.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TagCreateDto {
    @Getter
    @NoArgsConstructor
    @Builder
    @ApiModel("태그 등록 요청")
    static public class Request{
        @ApiModelProperty(notes = "태그", value = "학사")
        @Size(min = 1, max = 20)
        @NotEmpty
        private String text;

        public Request(@NotEmpty @Size(min = 1, max = 20) String text) {
            this.text = text;
        }
    }

    @Data
    @NoArgsConstructor
    @Builder
    static public class Response{

    }
}
