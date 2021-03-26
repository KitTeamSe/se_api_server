package com.se.apiserver.v1.board.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

public class BoardDeleteDto {

    @Data
    @AllArgsConstructor
    @Builder
    static public class Request{
        @Min(1)
        private Long boardId;

    }
}
