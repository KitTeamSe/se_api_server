package com.se.apiserver.domain.error.account;

import com.se.apiserver.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AccountErrorCode implements ErrorCode {

    NO_SUCH_ACCOUNT(400,"ME01", "존재하지 않는 사용자"),
    PASSWORD_INCORRECT(401,"ME02", "비밀번호 불일치");

    private int status;
    private final String code;
    private final String message;

    AccountErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
