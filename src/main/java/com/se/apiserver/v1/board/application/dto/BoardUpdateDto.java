package com.se.apiserver.v1.board.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

public class BoardUpdateDto {

    @Data
    @AllArgsConstructor
    @Builder
    @ApiModel("게시판 수정 요청")
    @NoArgsConstructor
    static public class Request{

        @ApiModelProperty(notes = "게시판 아이디", example = "1")
        private Long boardId;

        @ApiModelProperty(notes = "영문명", example = "freeboard")
        @Size(min = 2, max = 20)
        private String nameEng;

        @ApiModelProperty(notes = "한글명", example = "자유게시판")
        @Size(min = 2, max = 20)
        private String nameKor;
    }
}
