package com.se.apiserver.v1.notice.application.dto;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import io.swagger.annotations.ApiModel;
import lombok.*;

public class NoticeReadDto {
    @Data
    @Builder
    @ApiModel("알림 조회 응답")
    static public class ReadResponse {
        private String title;

        private String message;

        private String url;

        static public ReadResponse fromEntity(Notice notice) {
            ReadResponseBuilder readResponseBuilder = ReadResponse.builder()
                    .title(notice.getTitle())
                    .message(notice.getMessage())
                    .url(notice.getUrl());
            return readResponseBuilder.build();
        }
    }
}
