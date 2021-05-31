package com.se.apiserver.v1.noticerecord.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum NoticeRecordErrorCode implements ErrorCode {

    NO_SUCH_NOTICERECORD(400, "NR01", "존재하지 않는 알림 내역"),
    NO_SUCH_NOTICE(401, "NR02", "존재하지 않는 알림"),
    NO_SUCH_ACCOUNT(402, "NR03", "존재하지 않는 사용자");

    private int status;
    private final String code;
    private final String message;

    NoticeRecordErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
