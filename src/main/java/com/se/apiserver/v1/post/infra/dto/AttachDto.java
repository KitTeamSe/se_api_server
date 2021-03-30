package com.se.apiserver.v1.post.infra.dto;

import com.se.apiserver.v1.post.domain.entity.Attach;
import com.se.apiserver.v1.post.domain.entity.PostTagMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Size;

public class AttachDto {

    @ApiModel("첨부 파일")
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        @Size(min = 2, max = 255)
        @ApiModelProperty(notes = "다운로드 url", example = "test.test.com/file/test.jpg")
        private String downloadUrl;

        @Size(min = 2, max = 255)
        private String fileName;

        public Attach toEntity() {
            return Attach.builder()
                    .downloadUrl(downloadUrl)
                    .fileName(fileName)
                    .build();
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private Long attachId;

        private String downloadUrl;

        private String fileName;

        static public Response fromEntity(Attach attach) {
            return Response.builder()
                    .attachId(attach.getAttachId())
                    .downloadUrl(attach.getDownloadUrl())
                    .fileName(attach.getFileName())
                    .build();
        }
    }

}
