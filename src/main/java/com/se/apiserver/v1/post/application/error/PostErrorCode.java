package com.se.apiserver.v1.post.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum PostErrorCode implements ErrorCode {
    NO_SUCH_POST(400, "PO01", "존재하지 않는 게시글"),
    INVALID_INPUT(401, "PO02", "입력값이 올바르지 않음"),
    ONLY_ADMIN_SET_NOTICE(402, "PO03", "관리자만 공지글 설정 가능합니다"),
    CAN_NOT_ACCESS_POST(403, "PO04", "해당 게시글에 대한 권한이 없습니다."),
    ANONYMOUS_PASSWORD_INCORRECT(404, "PO05", "익명 게시글 비밀번호가 틀렸습니다."),
    NOT_ANONYMOUS_POST(405, "PO06", "익명으로 작성된 게시글이 아닙니다.");

    private int status;
    private final String code;
    private final String message;

    PostErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}