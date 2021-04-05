package com.se.apiserver.v1.board.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class BoardCreateDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("게시판 등록 요청")
    static public class Request{

        @ApiModelProperty(notes = "영문명", example = "freeboard")
        @Size(min = 2, max = 20)
        private String nameEng;

        @ApiModelProperty(notes = "한글명", example = "자유게시판")
        @Size(min = 2, max = 20)
        private String nameKor;
    }
}
