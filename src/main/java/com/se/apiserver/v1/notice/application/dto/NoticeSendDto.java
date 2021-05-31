package com.se.apiserver.v1.notice.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.Size;
import java.util.List;

public class NoticeSendDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("알림 전송 요청")
    @Builder
    static public class Request {

        @ApiModelProperty(notes = "tag_id", example = "tag_id")
        private List<Long> tagIdList;

        @ApiModelProperty(notes = "post_id", example = "post_id")
        private Long postId;

        @ApiModelProperty(notes = "title", example = "title")
        @Size(min = 10, max = 255)
        private String title;

        @ApiModelProperty(notes = "message", example = "message")
        @Size(min = 10, max = 255)
        private String message;

        @ApiModelProperty(notes = "url", example = "url")
        @Size(min = 10, max = 255)
        private String url;

    }

    @Builder
    @Getter
    static public class SendEntity {
        private List<Long> accountIdList;
        private String title;
        private String message;
        private String url;

        public SendEntity(List<Long> accountIdList, String title, String message, String url) {
            this.accountIdList = accountIdList;
            this.title = title;
            this.message = message;
            this.url = url;
        }
    }
}
