package com.se.apiserver.v1.notice.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

public class NoticeCreateDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("알림 등록 요청")
    static public class Request{

        @ApiModelProperty(notes = "title", example = "알림 제목")
        @Size(min = 3, max = 50)
        private String title;

        @ApiModelProperty(notes = "message", example = "알림 내용")
        @Size(min = 3, max = 50)
        private String message;

        @ApiModelProperty(notes = "url", example = "url")
        @Size(min = 10, max = 255)
        private String url;

    }
}
