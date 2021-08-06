package com.se.apiserver.v1.account.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AccountErrorCode implements ErrorCode {

    NO_SUCH_ACCOUNT(400, "ME01", "존재하지 않는 사용자"),
    PASSWORD_INCORRECT(400, "ME02", "비밀번호 불일치"),
    EMAIL_NOT_MATCH(400, "ME03", "이메일 불일치"),
    QA_NOT_MATCH(400, "ME04", "질문 응답 불일치"),
    EMAIL_VERIFY_TOKEN_EXPIRED(400, "ME05", "이메일 인증 시간 초과"),
    ALREADY_VERIFIED(400,"ME06", "이미 인증된 토큰"),
    NO_SUCH_QUESTION(400,"ME07", "존재하지 않은 질문"),
    DUPLICATED_NICKNAME(400,"ME08", "닉네임 중복"),
    DUPLICATED_STUDENT_ID(400,"ME09", "학번 중복"),
    QNA_INVALID_INPUT(400,"ME10", "올바르지 않은 QnA 질문 응답 쌍"),
    DUPLICATED_EMAIL(400,"ME11", "이메일 중복"),
    DUPLICATED_ID(400,"ME12", "아이디 중복"),
    CAN_NOT_ACCESS_ACCOUNT(400,"ME13", "해당 계정에 접근할 수 없습니다"),
    NO_SUCH_TOKEN(400, "ME14", "존재하지 않는 토큰");

    private int status;
    private final String code;
    private final String message;

    AccountErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
