package com.se.apiserver.v1.tag.application.dto;

import com.se.apiserver.v1.tag.domain.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TagReadDto {
    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @ApiModel("태그 아이디로 조회 요청")
    static public class ReadByIdRequest{
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    @ApiModel("태그 매치 요청")
    static public class ReadMatchRequest{
        @ApiModelProperty(notes = "태그", value = "학사")
        @Size(min = 2, max = 30)
        @NotEmpty
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{
        private Long tagId;
        private String text;

        public static Response fromEntity(Tag tag) {
            return Response.builder()
                    .tagId(tag.getTagId())
                    .text(tag.getText())
                    .build();
        }
    }
}
