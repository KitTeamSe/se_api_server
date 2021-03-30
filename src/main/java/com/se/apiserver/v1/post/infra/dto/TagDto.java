package com.se.apiserver.v1.post.infra.dto;

import com.se.apiserver.v1.post.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.PostTagMapping;
import com.se.apiserver.v1.tag.domain.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Size;

public class TagDto {

    @ApiModel("게시글 태그 등록")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Request{
        @ApiModelProperty(notes = "태그 아이디", example = "1")
        private Long tagId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{
        private Long tagId;

        private String tag;

        static public Response fromEntity(PostTagMapping postTagMapping){
            return Response.builder()
                    .tagId(postTagMapping.getTag().getTagId())
                    .tag(postTagMapping.getTag().getText())
                    .build();
        }
    }


}
