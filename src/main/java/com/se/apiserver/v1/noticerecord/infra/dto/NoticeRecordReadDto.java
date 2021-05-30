package com.se.apiserver.v1.noticerecord.infra.dto;

import com.se.apiserver.v1.noticerecord.domain.entity.NoticeRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeRecordReadDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("알림 내역 조회 요청")
    static public class Request{

        @ApiModelProperty(notes = "accountId", example = "accountId")
        private Long accountId;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static public class Response {

        private String title;
        private String message;
        private String url;
        private Long accountId;

        static public Response fromEntity(NoticeRecord noticeRecord) {
            return Response.builder()
                    .title(noticeRecord.getNotice().getTitle())
                    .message(noticeRecord.getNotice().getMessage())
                    .url(noticeRecord.getNotice().getUrl())
                    .accountId(noticeRecord.getAccount().getAccountId())
                    .build();
        }

    }

}
