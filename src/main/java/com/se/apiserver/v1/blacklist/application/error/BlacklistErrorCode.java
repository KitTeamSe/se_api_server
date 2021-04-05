package com.se.apiserver.v1.blacklist.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum BlacklistErrorCode implements ErrorCode {
    NO_SUCH_BLACKLIST(400,"BK01","존재하지 않는 블랙리스트"),
    DUPLICATED_BLACKLIST(401,"BK02","등록 ip 중복");
    private int status;
    private final String code;
    private final String message;

    BlacklistErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
