package com.se.apiserver.v1.notice.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum NoticeErrorCode implements ErrorCode {

    NO_SUCH_NOTICE(400,"NO01","존재하지 않는 알림");

    private int status;
    private final String code;
    private final String message;

    NoticeErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
