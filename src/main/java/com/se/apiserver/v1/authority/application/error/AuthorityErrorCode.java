package com.se.apiserver.v1.authority.application.error;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public enum AuthorityErrorCode implements ErrorCode {
    NO_SUCH_AUTHORITY(401, "AEO1", "권한이 존재하지 않음"),
    DUPLICATED_NAME_KOR(402, "AEO2", "권한 한글명 중복"),
    DUPLICATED_NAME_ENG(403, "AEO3", "권한 영문명 중복");

    private int status;
    private final String code;
    private final String message;

    AuthorityErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
