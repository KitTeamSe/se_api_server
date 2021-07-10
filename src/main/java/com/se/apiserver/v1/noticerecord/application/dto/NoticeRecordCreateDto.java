package com.se.apiserver.v1.noticerecord.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeRecordCreateDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("알림내역 등록 요청")
    static public class Request{

        @ApiModelProperty(example = "noticeRecordId", notes = "noticeRecordId")
        private Long noticeRecordId;

        @ApiModelProperty(example = "accountId", notes = "accountId")
        private Long accountId;

        @ApiModelProperty(notes = "noticeId", example = "noticeId")
        private Long noticeId;

        public Request(Long accountId, Long noticeId) {
            this.accountId = accountId;
            this.noticeId = noticeId;
        }
    }
}
