package com.se.apiserver.v1.tag.infra.dto.taglistening;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

public class TagListeningCreateDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("수신 태그 추가")
    static public class Request{
        @Min(1)
        private Long tagId;
    }
}
