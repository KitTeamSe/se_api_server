package com.se.apiserver.v1.taglistening.application.dto;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TagListeningCreateDto {
    @Getter
    @NoArgsConstructor
    @Builder
    @ApiModel("수신 태그 추가")
    static public class Request{
        @Min(1)
        private Long tagId;

        Request(@Min(1) Long tagId) {
            this.tagId = tagId;
        }
    }
}
