package com.se.apiserver.v1.blacklist.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class BlacklistCreateDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @ApiModel("블랙리스트 등록 요청")
    static public class Request{
        @ApiModelProperty(notes = "아이피", example = "127.0.0.1")
        @Size(min = 4, max = 20)
        String ip;

        @ApiModelProperty(notes = "사유", example = "광고성 댓글")
        @Size(min = 4, max = 20)
        String reason;
    }
}
