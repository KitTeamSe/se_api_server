package com.se.apiserver.v1.taglistening.application.dto;

import com.se.apiserver.v1.taglistening.domain.entity.TagListening;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

public class TagListeningReadDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response{

        private Long tagListeningId;

        @Min(1)
        private Long tagId;
        private String tagName;
        @Min(1)
        private Long accountId;
        private String accountIdString;

        static public Response fromEntity(TagListening tagListening){
            return Response.builder()
                    .accountId(tagListening.getAccount().getAccountId())
                    .accountIdString(tagListening.getAccount().getIdString())
                    .tagId(tagListening.getTag().getTagId())
                    .tagName(tagListening.getTag().getText())
                    .tagListeningId(tagListening.getTagListeningId())
                    .build();
        }
    }
}
