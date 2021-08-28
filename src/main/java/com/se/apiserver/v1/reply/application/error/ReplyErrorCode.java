package com.se.apiserver.v1.reply.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum ReplyErrorCode implements ErrorCode {
    NO_SUCH_REPLY(400, "RE01", "존재하지 않는 댓글"),
    INVALID_ANONYMOUS_INPUT(400, "RE02", "익명 사용자 정보 입력 오류"),
    INVALID_PASSWORD(400, "RE03", "익명 사용자 패스워드 틀림"),
    ALREADY_DELETED(400,"RE04", "삭제된 댓글"),
    NOT_ANONYMOUS_REPLY(400, "RE05", "익명으로 작성된 댓글이 아닙니다"),
    INVALID_REPLY(400, "RE06", "대댓글의 댓글은 작성할 수 없습니다"),
    CANNOT_REPLY_DELETED_PARENT(400, "RE07", "삭제된 댓글에 대댓글을 달 수 없습니다.");

    private int status;
    private final String code;
    private final String message;

    ReplyErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
