package com.se.apiserver.v1.reply.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ReplyErrorCode implements ErrorCode {
    NO_SUCH_REPLY(400, "RE01", "존재하지 않는 댓글"),
    INVALID_ANONYMOUS_INPUT(400, "RE02", "익명 사용자 정보 입력 오류"),
    INVALID_PASSWORD(400, "RE03", "익명 사용자 패스워드 틀림"),
    ALREADY_DELETED(400,"RE04", "삭제된 댓글");
    private int status;
    private final String code;
    private final String message;

    ReplyErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
