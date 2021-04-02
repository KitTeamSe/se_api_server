package com.se.apiserver.v1.reply.domain.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ReplyErrorCode implements ErrorCode {
    NO_SUCH_REPLY(400, "PO01", "존재하지 않는 댓글");
    private int status;
    private final String code;
    private final String message;

    ReplyErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
