package com.se.apiserver.v1.notice.infra.dto;

import com.se.apiserver.v1.notice.domain.entity.Notice;
import lombok.*;

public class NoticeReadDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static public class Request {
        private Long noticeId;
    }

    @Data
    @Builder
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
